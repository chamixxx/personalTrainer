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
}

