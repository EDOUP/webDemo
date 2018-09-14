app.service('contentService',function($http){
	//根据广告分类id查询广告
	this.findByCategoryId=function(categaryId){
		return $http.get('content/findByCategoryId.do?categaryId='+categaryId);
	}
});