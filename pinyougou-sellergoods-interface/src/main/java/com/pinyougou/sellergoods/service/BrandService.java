package com.pinyougou.sellergoods.service;

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;

import entity.PageResult;

/**
 * 品牌接口
 * @author Administrator
 *
 */
public interface BrandService {
	
	public List<TbBrand> findAll();
	
	/**
	 * 品牌分页
	 * @param pageNum 当前页面
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(int pageNum,int pageSize);
	
	/**
	 * 增加品牌
	 * @param brand
	 */
	public void  add(TbBrand brand);
	/**
	 * 根据Id 查询品牌
	 * @param id
	 * @return
	 */
	public TbBrand findOne(long id);
	
	/**
	 * 修改品牌信息
	 * @param brand
	 */
	public void update(TbBrand brand);
	
	/**
	 * 删除品牌信息
	 * @param ids
	 */
	public void delete(long[] ids);
	
	/**
	 * 条件查询+分页
	 * @param brand 查询条件
	 * @param pageNum 当前页数
	 * @param pageSize 当前记录数
	 * @return
	 */
	public PageResult findPage(TbBrand brand,int pageNum,int pageSize);
	
	/**
	 * 返回下拉列表数据
	 * @return
	 */
	public List<Map> selectOptionList();
}
