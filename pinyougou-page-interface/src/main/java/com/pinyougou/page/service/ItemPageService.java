package com.pinyougou.page.service;

public interface ItemPageService {
	
	/**
	 *   生成商品详情页
	 * @param goodsIds
	 * @return
	 */
	public boolean genItemHtml(Long goodsId);
	
	/**
	 * 删除商品详情页
	 * @param goodsIds
	 * @return
	 */
	public boolean deleteItemHtml(Long[] goodsIds);
}
