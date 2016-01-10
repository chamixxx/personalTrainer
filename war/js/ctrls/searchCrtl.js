angular.module('trainerApp').controller('searchCtrl',searchCtrlFnt);

searchCtrlFnt.$inject=['$scope', '$log', '$window', '$cookies', 'comm'];

function searchCtrlFnt($scope, $log, $window, $cookies, comm) {

	$scope.keyWord = "";

	var future = comm.getTrainings();
	  future.then(
	 	function(payload) {
	 		$scope.ListOfTrainings = payload;
	 		$log.info('list',$scope.ListOfTrainings);
	 	},
	 	function(errorPayload){
	 		$log.info('errorPayload',errorPayload)				
	 	}
	);

	$scope.openAddTraining = function () {
    	$window.location.assign("/ha-addTraining.html");
    };

    $scope.search = function () {
    	$cookies.put('keyWord', $scope.keyWord);
    	$window.location.assign("/ha-result-screen.html");
    };

    $scope.goToDetailsTrainings = function(name) {
    	$cookies.put('detailsName', name);
    	$cookies.put('type', "TrainingPlan");
    	$cookies.put('parentKey',"-1");
    	$window.location.assign("/ha-result-detail-screen.html");
    } 
}

