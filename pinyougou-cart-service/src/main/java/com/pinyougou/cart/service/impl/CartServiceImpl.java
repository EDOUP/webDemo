package com.pinyougou.cart.service.impl;

import java.io.Console;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.druid.sql.ast.expr.SQLCaseExpr.Item;
import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.cart.service.CartService;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbOrderItem;
import com.pinyougou.pojogroup.Cart;

@Service
public class CartServiceImpl implements CartService {
	
	private final Logger logger=LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private TbItemMapper itemMapper;
	@Override
	public List<Cart> addGoodsToCartList(List<Cart> cartList, Long itemId, Integer num) {
		//1.根据skId查询商品明细sku的对象
		TbItem item = itemMapper.selectByPrimaryKey(itemId);
		if(item==null) {
			throw new RuntimeException("商品不存在");
		}
		if(!item.getStatus().equals("1")) {
			throw new RuntimeException("商品状态不合法");
		}
		//2.根据sku对象得到商家ID
		String sellerId = item.getSellerId();
		//3.根据商家id在购物车列表中查询购物车对象
		Cart cart = searchCartBySellerId(cartList, sellerId);
		if(cart==null) {
			//4.如果购物车列表中不存在该商家的购物车
			//4.1创建一个新的购物车对象
			cart = new Cart();
			cart.setSellerId(sellerId);//设置商家id
			cart.setSellerName(item.getSeller());//商家名称
			List<TbOrderItem> orderItemList=new ArrayList<>();//创建购物车明细表
			TbOrderItem orderItem = createOrderItem(item, num);//创建新的购物车明细对象
			orderItemList.add(orderItem);
			cart.setOrderItemList(orderItemList);
			//4.2将新的购物车对象添加都购物车列表中
			cartList.add(cart);
			
		}else {
			//5.如果购物车列表中存在该商家的购物车
			//判断该商品是否存在该商家的明细列表中
			TbOrderItem orderItem = searchOrderItemByItemId(cart.getOrderItemList(),itemId);
			if(orderItem==null) {
				//5.1如果不存在，创建新的购物车明细对象
				orderItem = createOrderItem(item,num);
				cart.getOrderItemList().add(orderItem);
			}else {
				//5.2如果存在，在原有的数量上添加新的数量，并且更新金额
				orderItem.setNum(orderItem.getNum()+num);//更改数量
				orderItem.setTotalFee(new BigDecimal(orderItem.getPrice().doubleValue()*orderItem.getNum()));//金额
				//当冲突数量小于等于0移除磁明细
				if(orderItem.getNum()<=0) {
					cart.getOrderItemList().remove(orderItem);
				}
				//当购物车的数量为0时，在购物车中移除此购物车
				if(cart.getOrderItemList().size()<=0) {
					cartList.remove(cart);
				}
				
			}
			
		}
		
		return cartList;
	}
	/**
	 * 	根据skuID在购物车明细列表中查询购物车明细对象
	 * @param orderItemList
	 * @param itemId
	 * @return
	 */
	public TbOrderItem searchOrderItemByItemId(List<TbOrderItem> orderItemList,Long itemId) {
		for(TbOrderItem orderItem:orderItemList) {
			if(orderItem.getItemId().longValue()==itemId.longValue()) {
				return orderItem;
			}
		}
		return null;
	}
	
	/**
	 *	创建新的购物车明细对象
	 * @param num
	 * @param item
	 * @return
	 */
	private TbOrderItem createOrderItem( TbItem item,Integer num) {
		//创建新的购物车明细对象
		TbOrderItem orderItem = new TbOrderItem();
		orderItem.setGoodsId(item.getGoodsId());
		orderItem.setItemId(item.getId());
		orderItem.setNum(num);
		orderItem.setPrice(item.getPrice());
		orderItem.setTitle(item.getTitle());
		orderItem.setPicPath(item.getImage());
		orderItem.setSellerId(item.getSellerId());
		orderItem.setTotalFee(new BigDecimal(item.getPrice().doubleValue()*num));
		return orderItem;
	}
	/**
	 *	根据商家id在购物车列表中查询购物车对象
	 * @param cartList
	 * @param sellerId
	 * @return
	 */
	private Cart searchCartBySellerId(List<Cart> cartList, String sellerId) {
		for(Cart cart:cartList) {
			if(cart.getSellerId().equals(sellerId)) {
				return cart;
			}
		}
		return null;
	}
	
	@Autowired
	private RedisTemplate redisTemplate;
	
	
	@Override
	public List<Cart> findCartList(String username) {
		logger.info("从Redis中提取购物车数据"+username);
		List<Cart> cartList = (List<Cart>) redisTemplate.boundHashOps("cartList").get(username);
		if(cartList==null) {
			cartList=new ArrayList();
		}
		return cartList;
	}
	@Override
	public void saveCartListRedis(String username, List<Cart> cartList) {
		logger.info("向Redis中存入购物车"+username);
		redisTemplate.boundHashOps("cartList").put(username, cartList);
	}
	
	@Override
	public List<Cart> mergeCartList(List<Cart> cartList1, List<Cart> cartList2) {
		//cartList1.addAll(cartList2);//不能简单合并
		for(Cart cart:cartList2) {
			for(TbOrderItem orderItem:cart.getOrderItemList()) {
				cartList1 = addGoodsToCartList(cartList1, orderItem.getItemId(), orderItem.getNum());
			}
		}
		
		
		return cartList1;
	}

}
