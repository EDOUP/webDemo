app.controller('searchController', function($scope,$location,searchService) {
	
	//定义搜索对象的结构
	$scope.searchMap={'keywords':'','category':'','brand':'','spec':{},'price':'','pageNo':1,'pageSize':40,'sort':'','sortField':''};
	//搜索
	$scope.search=function(){
		$scope.searchMap.pageNo=parseInt($scope.searchMap.pageNo);
		searchService.search($scope.searchMap).success(function(response) {
			$scope.resultMap=response;
			buildPageLabel();	//构建分页栏
		});
	}
	
	//构建分页栏
	buildPageLabel=function(){
		$scope.pageLabel=[];
		
		var firstPage=1;//开始页码
		var lastPage=$scope.resultMap.totalPages;//截止页码
		$scope.firstDot=true;//前面有...
		$scope.lastDot=true;//后面有...
		if($scope.resultMap.totalPages>5){//如果页码数量大于5
			if($scope.searchMap.pageNo<=3){//如果当前页码小于等于3
				lastPage=5;
				$scope.firstDot=false;
				}else if($scope.searchMap.pageNo>=$scope.resultMap.totalPages-2){//显示后五页
					firstPage=$scope.resultMap.totalPages-4;
					$scope.lastDot=false;
					}else{
						firstPage=$scope.searchMap.pageNo-2;
						lastPage=$scope.searchMap.pageNo+2;
						}
			}else{
				$scope.firstDot=false;
				$scope.lastDot=false;
			}
		for(var i=firstPage;i<=lastPage;i++){
			$scope.pageLabel.push(i);
		}
		
	}
	
	//添加搜索项,改变searchMap的值
	$scope.addSearchItem=function(key,value){
		if(key=='category'||key=='brand'||key=='price'){//用户点击分类或者是品牌
			$scope.searchMap[key]=value;
		}else{//规格
			$scope.searchMap.spec[key]=value;
		}
		$scope.search();//查询
	}
	
	//撤销搜索项
	$scope.removeSearchItem=function(key){
		if(key=='category'||key=='brand'||key=='price'){//用户点击分类或者是品牌
			$scope.searchMap[key]="";
		}else{//规格
			delete $scope.searchMap.spec[key];
		}
		$scope.search();//查询
	}
	
	//分页查询
	$scope.queryByPage=function(pageNo){
		if(pageNo<1||pageNo>$scope.resultMap.totalPages){
			return ;
		}
		$scope.searchMap.pageNo=pageNo;
		$scope.search();
	}
	//判断当前页是否是第一页
	$scope.isTopPage=function(){
		if($scope.searchMap.pageNo==1){
			return true;
		}else{
			return false;
		}
	}
	//判断当前页是否是最后一页
	$scope.isEndPage=function(){		
		if($scope.searchMap.pageNo==$scope.resultMap.totalPages){
			return true;
		}else{
			return false;
		}
	}
	//排序查询
	$scope.sortSearch=function(sortField,sort){
		$scope.searchMap.sortField=sortField;
		$scope.searchMap.sort=sort;
		
		$scope.search();//查询
	}
	//判断关键字是否是品牌
	$scope.keywordsIsBrand=function(){
		for(var i=0;i<$scope.resultMap.brandList.length;i++){
			if($scope.searchMap.keywords.indexOf($scope.resultMap.brandList[i].text)>=0){
				return true;
			}
		}
		return false;
	}
	
	//接收首页并加载关键字
	$scope.loadkeywords=function(){
		$scope.searchMap.keywords=$location.search()['keywords'];
		$scope.search();
	}
})