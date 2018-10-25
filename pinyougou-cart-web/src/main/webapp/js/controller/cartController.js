app.controller('cartController', function($scope,cartService) {
	
	//查询购物车列表
	$scope.findCartList=function(){
		cartService.findCartList().success(function(response) {
			$scope.cartList=response;
			$scope.totalValue=cartService.sum($scope.cartList);
		})
	}
	//数量加减
	$scope.addGoodsToCartList=function(itemId,num){
		cartService.addGoodsToCartList(itemId,num).success(function(response) {
			if(response.success){
				$scope.findCartList();//刷新列表
			}else{
				alert(response.message);
			}
		})
	}
	//获取当前用户的地址列表
	$scope.findAddressList=function(){
		cartService.findAddressList().success(
			function(response){
				$scope.addressList=response;
			}		
		);		
	}
	//选择地址
	$scope.selectAddress=function(address){
		$scope.address=address;
	}
	//判断某对象是不是当前选择地址
	$scope.isSelectedAddress=function(address){
		if(address==$scope.address){
			return true;
		}else{
			return false;
		}
	}
	$scope.order={paymentType:'1'};//订单对象
	
	//选择支付方式
	$scope.selectPaytype=function(type){
		$scope.order.paymentType=type;
	}
	
	$scope.submitOrder=function(){
		$scope.order.receiverAreaName=$scope.address.address;//地址
		$scope.order.receiverMobile=$scope.address.mobile;//手机
		$scope.order.receiver=$scope.contact;//联系人
		cartService.submit($scope.order).success(function(response) {
			if(response.success){//页面跳转
				if($scope.order.paymentType=='1'){//如果是微信支付，跳转到支付页面
					location.href="pay.html";
				}else{//如果货到付款，跳转到提示页面
					location.href="paysuccess.html";
				}
			}else{
				alert(response.message);//也可以调到提示页面
			}
			
		})
	}
	
})