angular.module('trainerApp').controller('resultsCtrl',resultsCtrlFnt);

resultsCtrlFnt.$inject=['$scope', '$log', '$window', '$cookies', 'comm'];

function resultsCtrlFnt($scope, $log, $window, $cookies, comm) {
	$scope.keyWord = $cookies.get('keyWord');
	$scope.keyWord2 = "";

	var future = comm.getSearchResults($scope.keyWord);
	future.then(
	 	function(payload) {
	 		$scope.resultsSearch = payload;
	 		$log.info('list',$scope.resultsSearch);
	 	},
	 	function(errorPayload){
	 		$log.info('errorPayload',errorPayload)				
	 	}
	);
	
	$scope.openAddTraining = function () {
    	$window.location.assign("/ha-addTraining.html");
    };

     $scope.search = function () {
     	var future = comm.getSearchResults($scope.keyWord2);
	future.then(
	 	function(payload) {
	 		$scope.resultsSearch = payload;
	 		$log.info('list',$scope.resultsSearch);
	 	},
	 	function(errorPayload){
	 		$log.info('errorPayload',errorPayload)				
	 	}
	);	
    };

}