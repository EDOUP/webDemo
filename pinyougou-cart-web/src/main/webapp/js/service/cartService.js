app.service('cartService',function($http){
	//查询购物车列表
	this.findCartList=function(){
		return $http.get('cart/findCartList.do');
	}
	//添加商品到购物车
	this.addGoodsToCartList=function(itemId,num){
		return $http.get('cart/addGoodsToCartList.do?itemId='+itemId+'&num='+num);
	}
	
	//求购物车商品合计
	this.sum=function(cartList){
		var totalValue={totalNum:0,totalMoney:0}
		
		for(var i=0;i<cartList.length;i++){
			var cart=cartList[i];//购物车对象
			for(var j=0;j<cart.orderItemList.length;j++){
				var orderItem=cart.orderItemList[j];
				totalValue.totalNum+=orderItem.num;
				totalValue.totalMoney+=orderItem.totalFee;
			}
		}
		
		return totalValue;
	}
	
	//获取地址列表
	this.findAddressList=function(){
		return $http.get('address/findListByLoginUser.do');	
	}
	
	//提交订单
	this.submit=function(order){
		return $http.post('order/add.do',order)
	}
	
})