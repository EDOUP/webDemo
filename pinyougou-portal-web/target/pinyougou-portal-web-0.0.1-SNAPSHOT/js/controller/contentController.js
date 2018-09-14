app.controller('contentController',function($scope,contentService){
	
	$scope.contentList=[];//广告列表集合
	$scope.findByCategoryId=function(categaryId){
		contentService.findByCategoryId(categaryId).success(function(response) {
			$scope.contentList[categaryId]=response;
			
		});
	}
	//搜索传递参数
	$scope.search=function(){
		location.href="http://localhost:9104/search.html#?keywords="+$scope.keywords;
	}

});