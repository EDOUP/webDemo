app.controller('brandController',function($scope,$controller,brandService){	

	$controller('baseController',{$scope:$scope});
	
	//查询商品列表
			$scope.findAll=function(){
				brandService.findAll().success(
	 					function(response){
	 						$scope.list=response;
	 					}
	 				);
	 			}
	 			
	 			
	 			
	 			//分页方法
	 			$scope.findPage=function(page,size){	
	 				brandService.findPage(page,size).success(
	 						function(response){
	 							$scope.list=response.rows;	//显示当前页数据
	 							$scope.paginationConf.totalItems=response.total;//更新总记录数
	 						}			
	 				);
	 			}
	 			
	 			//新增品牌
	 			$scope.save=function(){
	 				var object=null;//方法名
	 				if($scope.entity.id!=null){
	 					object=brandService.update($scope.entity);
	 				}else{
	 					object=brandService.add($scope.entity);
	 				}
	 				
	 				object.success(
	 					function(response){
	 						if(response.success){
	 							 $scope.reloadList();//重新加载,局部刷新
	 						}else{
	 							alert(response.message);
	 						}
	 					}		
	 				);
	 			}
	 			
	 			//查询品牌实体
	 			$scope.findOne=function(id){
	 				brandService.findOne(id).success(
	 				function(response){
	 						$scope.entity=response;	
	 				});
	 			}
	 			
	 			
	 			
	 			
	 			//删除品牌信息
	 			$scope.deleteSelectsIds=function(){
	 				brandService.deleteSelectsIds($scope.selectIds).success(
		 					function(response){
		 						if(response.success){
		 							 $scope.reloadList();//重新加载,局部刷新
		 						}else{
		 							alert(response.message);
		 						}
		 					}		
		 				);
	 			}
	 			$scope.searchEntity={};
	 			//条件查询
	 			$scope.search=function(page,size){
	 				brandService.search(page,size,$scope.searchEntity).success(
	 						function(response){
	 							$scope.list=response.rows;	//显示当前页数据
	 							$scope.paginationConf.totalItems=response.total;//更新总记录数
	 						}			
	 				);
	 			}
	 			
	 		});