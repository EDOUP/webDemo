package com.pinyougou.pay.service;

import java.util.Map;

public interface WeixinPayService {
	
	/**
	 * 	生成二维码
	 * @param out_trade_no 商户订单号
	 * @param total_fee 标价金额
	 * @return
	 */
	public Map creatNative(String out_trade_no,String total_fee);
	
	/**
	 * 查询支付状态
	 * @param out_trade_no
	 */
	public Map queryPayStatus(String out_trade_no);
	
	/**
	 * 关闭支付
	 * @param out_trade_no
	 * @return
	 */
	public Map closePay(String out_trade_no);

}
