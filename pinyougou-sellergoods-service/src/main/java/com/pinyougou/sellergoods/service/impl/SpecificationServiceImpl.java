package com.pinyougou.sellergoods.service.impl;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbSpecificationMapper;
import com.pinyougou.mapper.TbSpecificationOptionMapper;
import com.pinyougou.pojo.TbSpecification;
import com.pinyougou.pojo.TbSpecificationExample;
import com.pinyougou.pojo.TbSpecificationExample.Criteria;
import com.pinyougou.pojo.TbSpecificationOption;
import com.pinyougou.pojo.TbSpecificationOptionExample;
import com.pinyougou.pojogroup.Specification;
import com.pinyougou.sellergoods.service.SpecificationService;

import entity.PageResult;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class SpecificationServiceImpl implements SpecificationService {

	@Autowired
	private TbSpecificationMapper specificationMapper;
	
	@Autowired
	private TbSpecificationOptionMapper specificationOptionMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbSpecification> findAll() {
		return specificationMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbSpecification> page=   (Page<TbSpecification>) specificationMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Specification specification) {
		
		//获得实体名称
		TbSpecification tbspecification =specification.getSpecification();
		specificationMapper.insert(tbspecification );		
		
		//获得规格选项集合
		List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
		for(TbSpecificationOption option: specificationOptionList) {
			option.setSpecId(tbspecification.getId());//设置规格id
			 specificationOptionMapper.insert(option);//新增规格
		}
		
	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Specification specification){
		//获取规格实体
		TbSpecification tbSpecification = specification.getSpecification();
		//插入specification中
		specificationMapper.updateByPrimaryKey(tbSpecification);
		
		//先删除原来的规格对应的数据，该功能很少修改，可不考虑数据库性能
		//获取规格列表
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		//拼接两个表查询条件
		com.pinyougou.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(tbSpecification.getId());
		specificationOptionMapper.deleteByExample(example);
		//获得规格选项集合
		List<TbSpecificationOption> specificationOptionList = specification.getSpecificationOptionList();
		for(TbSpecificationOption option: specificationOptionList) {
			option.setSpecId(tbSpecification.getId());//设置规格id
			 specificationOptionMapper.insert(option);//新增规格
			
		}
		
	}	
	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Specification findOne(Long id){
		Specification specification = new Specification();
		//获取规格实体
		TbSpecification tbSpecification = specificationMapper.selectByPrimaryKey(id);
		//插入specification中
		specification.setSpecification(tbSpecification);

		//获取规格列表
		TbSpecificationOptionExample example = new TbSpecificationOptionExample();
		//拼接两个表查询条件
		com.pinyougou.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
		criteria.andSpecIdEqualTo(id);
		//获取spec_Id==id的值
		List<TbSpecificationOption> specificationOptionList= specificationOptionMapper.selectByExample(example);
		specification.setSpecificationOptionList(specificationOptionList);
		//返回组合实体类
		return specification;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			//删除规格表数据
			specificationMapper.deleteByPrimaryKey(id);
			//删除规格选项表数据
			
			
			TbSpecificationOptionExample example =new TbSpecificationOptionExample();
			com.pinyougou.pojo.TbSpecificationOptionExample.Criteria criteria = example.createCriteria();
			criteria.andSpecIdEqualTo(id);
			specificationOptionMapper.deleteByExample(example);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbSpecification specification, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbSpecificationExample example=new TbSpecificationExample();
		Criteria criteria = example.createCriteria();
		
		if(specification!=null){			
						if(specification.getSpecName()!=null && specification.getSpecName().length()>0){
				criteria.andSpecNameLike("%"+specification.getSpecName()+"%");
			}
	
		}
		
		Page<TbSpecification> page= (Page<TbSpecification>)specificationMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}

		@Override
		public List<Map> selectOptionList() {
			// TODO Auto-generated method stub
			return specificationMapper.selectOptionList();
		}
	
}
