package com.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;
import com.pinyougou.pojo.TbBrandExample;
import com.pinyougou.pojo.TbBrandExample.Criteria;
import com.pinyougou.sellergoods.service.BrandService;

import entity.PageResult;

@Service
@Transactional
public class BrandServiceImpl implements BrandService {

	@Autowired
	private TbBrandMapper  brandMapper;
	
	@Override
	public List<TbBrand> findAll() {
		//查询全部数据
		return brandMapper.selectByExample(null);
	}

	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		//分页，dao配置文件中加载
		PageHelper.startPage(pageNum, pageSize);
		//查询全部数据,强转为page类
		Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);
		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public void add(TbBrand brand) {		
			brandMapper.insert(brand);			
	}

	@Override
	public TbBrand findOne(long id) {
		return brandMapper.selectByPrimaryKey(id);
	}

	@Override
	public void update(TbBrand brand) {
		brandMapper.updateByPrimaryKey(brand);
		
	}

	@Override
	public void delete(long[] ids) {
		for(long id :ids) {
			brandMapper.deleteByPrimaryKey(id);
		}
		
	}

	@Override
	public PageResult findPage(TbBrand brand, int pageNum, int pageSize) {
		//分页，dao配置文件中加载
		PageHelper.startPage(pageNum, pageSize);
		
		
		TbBrandExample example =new TbBrandExample();
		//构建条件
		Criteria criteria = example.createCriteria();
		if(brand!=null) {
			if(brand.getName()!=null && brand.getName().length()>0) {
				//条件拼接
				criteria.andNameLike("%"+brand.getName()+"%");
			}
			if(brand.getFirstChar()!=null && brand.getFirstChar().length()>0) {
				criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");				
			}
		}

		//查询全部数据,强转为page类
		Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example );
		
		return new PageResult(page.getTotal(), page.getResult());
	}

	@Override
	public List<Map> selectOptionList() {
		
		return brandMapper.selectOptionList(); 
	}

}
