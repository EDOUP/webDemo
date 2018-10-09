 //控制层 
app.controller('userController' ,function($scope,$controller   ,userService){	
	
	//$controller('baseController',{$scope:$scope});//继承
	//比较两次密码是否一致

	$scope.reg=function(){
		if($scope.password!=$scope.entity.password){
			alert("两次输入密码不一致，请重新输出");
			$scope.password="";
			return;
		}
		//新增
		userService.add($scope.entity,$scope.emailcode).success(
				function(response){
					alert(response.message);
				});
	}
	//发送邮箱验证码
	$scope.sendEmailCode=function(){
		if($scope.entity.email==null||$scope.entity.email==""){
			alert("请填写邮箱账号")
			return;
		}
		userService.sendEmailCode($scope.entity.email).success(function(response) {
			alert(response.message);
		});
	}
});	
