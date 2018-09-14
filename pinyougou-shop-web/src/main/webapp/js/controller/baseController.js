app.controller('baseController',function($scope){
	
	//分页控件配置 
		$scope.paginationConf = {
				 currentPage: 1,//当前页
				 totalItems: 10,//总记录数
				 itemsPerPage: 10,//每页记录数
				 perPageOptions: [10, 20, 30, 40, 50],//分页选项
				 onChange: function(){//当页面变更触发					 	
				        	 $scope.reloadList();//重新加载
				 }
		}; 
		
		//刷新列表
		$scope.reloadList=function(){
		//	$scope.findPage($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
			$scope.search($scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);//替代
		}
		
		$scope.selectIds=[];//用户勾选复选框id值
			
			//获取勾选的复选框id值并实时更新
			$scope.updateSelection=function($event,id){
				if($event.target.checked){//被选中
				$scope.selectIds.push(id);
				}else{//取消选中
					var index=$scope.selectIds.indexOf(id);//查找值得位置
					$scope.selectIds.splice(index,1);//参数：移除位置，个数
				}
			}
	
			//优化列表显示
		$scope.jsonToString=function(jsonString,key){
			var json =JSON.parse(jsonString);
			var value="";
			for(var i=0;i<json.length;i++){
				if(i>0){
					value+=",";
				}
				value +=json[i][key];
			}
			return value;
		}
		
		//在list集合中根据key的值查询对象
		$scope.searchObjectByKey=function(list,key,keyValue){
			for(var i=0;i<list.length;i++){
				if(list[i][key]==keyValue){
					return list[i];
				}
			}
			return null;
		}
	
});