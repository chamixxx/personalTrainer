angular.module('trainerApp').controller('statCtrl',statCtrlFnt);

statCtrlFnt.$inject=['$scope', '$log', '$window', '$cookies', 'comm'];

function statCtrlFnt($scope, $log, $window, $cookies, comm) {

	$scope.stats;
	$scope.userId = $cookies.get('userId');

	var future = comm.getStats($scope.userId);
	future.then(
	 	function(payload) {
	 		$scope.stats = payload;
	 		$log.info('Stats',$scope.stats);
	 	},
	 	function(errorPayload){
	 		$log.info('errorPayload',errorPayload)				
	 	}
	);

	




	$scope.back = function() {
		$window.history.back();
	}  


}