package com.pinyougou.page.service.impl;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbGoodsDescExample;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import com.pinyougou.pojo.TbItemExample.Criteria;
import com.pinyougou.pojogroup.Goods;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateNotFoundException;

@Service
public class ItemPageServiceImpl implements ItemPageService {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Value("${pagedir}")
	private String pagedir; 
	
	@Autowired
	private TbGoodsMapper  goodsMapper;
	
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	
	@Autowired
	private TbItemCatMapper itemCatMapper;
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Override
	public boolean genItemHtml(Long goodsId) {
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		
		try {
			Template template = configuration.getTemplate("item.ftl");
			//创建数据模型
			Map dataModel=new HashMap<>();
			//1.商品主表数据
			TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goods", goods);
			
			//2.商品扩展表数据
			TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
			dataModel.put("goodsDesc", goodsDesc);
			
			//3.读取商品分类
			String itemCat1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();
			String itemCat2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();
			String itemCat3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();
			dataModel.put("itemCat1", itemCat1);
			dataModel.put("itemCat2", itemCat2);
			dataModel.put("itemCat3", itemCat3);
			
			//4.读取sku列表
			TbItemExample example=new TbItemExample();
			Criteria criteria = example.createCriteria();
			criteria.andGoodsIdEqualTo(goodsId);//spu id
			criteria.andStatusEqualTo("1");//状态有效
			example.setOrderByClause("is_default desc");//按是否默认降序排列，保证第一个是默认的sku
			List<TbItem> itemList = itemMapper.selectByExample(example);
			dataModel.put("itemList", itemList);
			
			
			Writer out =new FileWriter(pagedir+goodsId+".html");
			
			//输出
			template.process(dataModel, out);
			out.close();
			
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} 
		
	}

}