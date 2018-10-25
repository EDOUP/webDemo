package com.pinyougou.seckill.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.pay.service.WeixinPayService;
import com.pinyougou.pojo.TbPayLog;
import com.pinyougou.pojo.TbSeckillOrder;
import com.pinyougou.seckill.service.SeckillOrderService;

import entity.Result;
import util.IdWorker;

@RestController
@RequestMapping("/pay")
public class PayController {
	
	@Reference
	private WeixinPayService weixinPayService;
	
	@Reference
	private SeckillOrderService seckillOrderService;
	
	@RequestMapping("/creatNative")
	public Map creatNative() {
		//1.获取当前用户		
		String username=SecurityContextHolder.getContext().getAuthentication().getName();
		//2.提取秒杀订单
		TbSeckillOrder seckillOrder = seckillOrderService.searchOrderFromRedisByUserId(username);
		//判断支付日志存在
		if(seckillOrder!=null){
			return weixinPayService.creatNative(seckillOrder.getId()+"",(long)(seckillOrder.getMoney().doubleValue()*100)+"");
		}else{
			return new HashMap();
		}		
	}

	/**
	 * 查询支付状态
	 * @param out_trade_no
	 * @return
	 */  
	@RequestMapping("/queryPayStatus")
	public Result queryPayStatus(String out_trade_no){
		//获取当前用户		
		String userId=SecurityContextHolder.getContext().getAuthentication().getName();
		Result result=null;		
		int x=0;		
		while(true){
			//调用查询接口
			Map<String,String> map = weixinPayService.queryPayStatus(out_trade_no);
			if(map==null){//出错			
				result=new  Result(false, "支付出错");
				break;
			}			
			if(map.get("trade_state").equals("SUCCESS")){//如果成功				
				result=new  Result(true, "支付成功");				
				seckillOrderService.saveOrderFromRedisToDb(userId, Long.valueOf(out_trade_no), map.get("transaction_id"));
				break;
			}			
			try {
				Thread.sleep(3000);//间隔三秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
			x++;//设置超时时间为5分钟
			if(x>20){
				result=new  Result(false, "二维码超时");	
				//1.调用微信的关闭订单接口
				Map<String,String> payresult = weixinPayService.closePay(out_trade_no);				
				if( !"SUCCESS".equals(payresult.get("result_code")) ){//如果返回结果是正常关闭
					if("ORDERPAID".equals(payresult.get("err_code"))){
						result=new Result(true, "支付成功");	
						seckillOrderService.saveOrderFromRedisToDb(userId, Long.valueOf(out_trade_no), map.get("transaction_id"));
					}					
				}				
				if(result.isSuccess()==false){
					System.out.println("超时，取消订单");
					//2.调用删除
					seckillOrderService.deleteOrderFromRedis(userId, Long.valueOf(out_trade_no));	
				}				
				break;
			}			
		}
		return result;
	}
}
