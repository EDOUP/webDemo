package com.pinyougou.search.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.FilterQuery;
import org.springframework.data.solr.core.query.GroupOptions;
import org.springframework.data.solr.core.query.HighlightOptions;
import org.springframework.data.solr.core.query.HighlightQuery;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleFilterQuery;
import org.springframework.data.solr.core.query.SimpleHighlightQuery;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.GroupEntry;
import org.springframework.data.solr.core.query.result.GroupPage;
import org.springframework.data.solr.core.query.result.GroupResult;
import org.springframework.data.solr.core.query.result.HighlightEntry;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;
import org.springframework.data.solr.core.query.result.HighlightPage;
import org.springframework.data.solr.core.query.result.ScoredPage;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;

@Service(timeout=5000)
public class ItemSearchServiceImpl implements ItemSearchService{

	@Autowired
	private SolrTemplate solrTemplate;
	
	
	@Override
	public Map search(Map searchMap) {
		
		Map map=new HashMap();
		//处理空格
		String  keywords=(String) searchMap.get("keywords");
		searchMap.put("keywords", keywords.replace(" ", ""));//去掉空格
		//1.查询列表
		 map.putAll(searchList(searchMap));
		 //2.分组查询商品分类列表
		 List<String> categoryList = searchCategoryList(searchMap);
		 map.put("categoryList", categoryList);
		 //3.查询品牌和规格列表
		 String category=(String) searchMap.get("category");
		 if(!category.equals("")) {
			 map.putAll(searchBrandAndSpecList(category));
		 }else {
			 
			 if(categoryList.size()>0) {
				 map.putAll(searchBrandAndSpecList(categoryList.get(0)));
			 }
			 
		 }
		 
		return map;	
	}
	/**
	 * 查询列表
	 * @param searchMap
	 * @return
	 */
	private Map searchList(Map searchMap) {
		Map map=new HashMap();
		//高亮显示
		HighlightQuery query=new SimpleHighlightQuery();
		//构建高亮域对象
		HighlightOptions highlightOptions = new HighlightOptions().addField("item_title");//高亮域
		highlightOptions.setSimplePrefix("<em style='color:red'>");//前缀
		highlightOptions.setSimplePostfix("</em>");//后缀
		query.setHighlightOptions(highlightOptions);//为查询对象设置高亮选项
		
		//1.1查询关键字
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		//1.2按商品分类过滤查询
		if(!"".equals(searchMap.get("category"))) {//如果选择条件不为空
			FilterQuery filterQuery=new SimpleFilterQuery();
			Criteria filterCriteria=new Criteria("item_category").is(searchMap.get("category"));
			filterQuery.addCriteria(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		//1.3按品牌过滤查询
		if(!"".equals(searchMap.get("brand"))) {//如果选择条件不为空
			FilterQuery filterQuery=new SimpleFilterQuery();
			Criteria filterCriteria=new Criteria("item_brand").is(searchMap.get("brand"));
			filterQuery.addCriteria(filterCriteria);
			query.addFilterQuery(filterQuery);
		}
		//1.4按规格过滤
		if(searchMap.get("spec")!=null) {
			Map<String,String> specMap=(Map<String, String>) searchMap.get("spec");
			for(String key:specMap.keySet()) {
				FilterQuery filterQuery=new SimpleFilterQuery();
				Criteria filterCriteria=new Criteria("item_spec_"+key).is(searchMap.get(key));
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);
			}
		}
		//1.5按价格过滤
		if(!"".equals(searchMap.get("price"))) {
			String[] price=	((String)searchMap.get("price")).split("-");
			if(!price[0].equals("0")) {//如果最低价费不等于0
				FilterQuery filterQuery=new SimpleFilterQuery();
				Criteria filterCriteria=new Criteria("item_price").greaterThanEqual(price[0]);
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);		
			}
			if(!price[1].equals("*")) {//如果最高价格不等于*(没有限制)
				FilterQuery filterQuery=new SimpleFilterQuery();
				Criteria filterCriteria=new Criteria("item_price").lessThanEqual(price[1]);
				filterQuery.addCriteria(filterCriteria);
				query.addFilterQuery(filterQuery);		
			}
		}
		//1.6分页
		Integer pageNo=(Integer)searchMap.get("pageNo");//页码
		if(pageNo==null) {
			pageNo=1;
		}
		Integer pageSize=(Integer)searchMap.get("pageSize");//页码大小
		if(pageSize==null) {
			pageSize=20;
		}
		query.setOffset((pageNo-1)*pageSize);//起始索引
		query.setRows(pageSize);//每页记录数
		//1.7排序
		String sortValue=(String)searchMap.get("sort");//排序方式
		String sortField=(String)searchMap.get("sortField");//排序字段
		if(sortValue!=null&&!sortField.equals("")) {
			if(sortValue.equals("ASC")){
				Sort sort=new Sort(Sort.DEFAULT_DIRECTION.ASC,"item_"+sortField);//升序
				query.addSort(sort);
			}
			if(sortValue.equals("DESC")){
				Sort sort=new Sort(Sort.DEFAULT_DIRECTION.DESC,"item_"+sortField);//降序
				query.addSort(sort);
			}
		}
		
		
		
		//获取高亮结果集
		//高亮页对象
		HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);
		//高亮入口集合(每条记录的高亮入口)
		List<HighlightEntry<TbItem>> entryList = page.getHighlighted();
		for(HighlightEntry<TbItem> entry : entryList) {
			//获取高亮列表(高亮域的个数)
			
			List<Highlight> highlights = entry.getHighlights();
			
			/*for(Highlight h:highlights) {
				List<String> sns = h.getSnipplets();//每个域可能存储多个值
				System.out.println(sns);
			}*/
		
			if(highlights.size()>0 && highlights.get(0).getSnipplets().size()>0) {
			TbItem item = entry.getEntity();
			item.setTitle(highlights.get(0).getSnipplets().get(0));
			}
		}
		map.put("rows",page.getContent());
		map.put("totalPages", page.getTotalPages());//总页数
		map.put("total", page.getTotalElements());//总记录数
		return map;
		
	}
	/**
	 * 分组查询
	 * @param searchMap
	 * @return
	 */
	private List<String> searchCategoryList(Map searchMap) {
		List<String> list=new ArrayList();
		
		Query query=new SimpleQuery("*:*");
		//where:关键字查询
		Criteria criteria=new Criteria("item_keywords").is(searchMap.get("keywords"));
		query.addCriteria(criteria);
		
		//Group by:设置分组选项
		GroupOptions groupOptions=new GroupOptions().addGroupByField("item_category");
		query.setGroupOptions(groupOptions);
		//获取分组页
		GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);	
		//获取分组结果对象
		GroupResult<TbItem> groupResult = page.getGroupResult("item_category");
		//获取分组入口页
		Page<GroupEntry<TbItem>> groupEntries = groupResult.getGroupEntries();
		//获取分组入口集合
		List<GroupEntry<TbItem>> entryList = groupEntries.getContent();
		
		for(GroupEntry<TbItem> entry:entryList) {
			list.add(entry.getGroupValue());//将分组的结果添加到返回组中
		}
		
		return list;
		
	}
	
	@Autowired
	private	RedisTemplate redisTemplate;
	
	/**
	 * 根据品牌分类名称查询品牌和规格列表
	 * @param category品牌分类名称
	 * @return
	 */
	
	private Map searchBrandAndSpecList(String category) {
		
		Map map=new HashMap();
		
		//1.根据商品分类名称德奥模板id
		Long templateId  =(Long) redisTemplate.boundHashOps("itemCat").get(category);
		if(templateId!=null) {
		//2.根据模板id获取品牌列表
		List brandList=(List)redisTemplate.boundHashOps("brandList").get(templateId);
		map.put("brandList",brandList);
		//3.根据模板id获取规格列表
		List specList=(List)redisTemplate.boundHashOps("specList").get(templateId);
		map.put("specList", specList);
		}
		return map;
		
	}
	
	@Override
	public void importList(List list) {
		solrTemplate.saveBeans(list);
		solrTemplate.commit();
	}
	
	@Override
	public void deleteByGoodsIds(List goosIds) {
		Query query = new SimpleQuery("*:*");
		Criteria criteria = new Criteria("item_goodsid").in(goosIds);
		query.addCriteria(criteria);
		solrTemplate.delete(query);
		solrTemplate.commit();
		
	}

}
