angular.module('trainerApp').controller('welcomeCtrl',welcomeCtrlFnt);

welcomeCtrlFnt.$inject=['$scope', '$log', 'comm'];

function welcomeCtrlFnt($scope, $log, comm){

	$scope.message = "";
	$scope.messageLoaded = false;

	$scope.loadMessage=function() {
		
		 var future = comm.getMessage();
		 future.then(
		 	function(payload) {
		 		$log.info('payload',payload);	
		 		$scope.message = payload;
		 		$scope.messageLoaded = true;
		 	},
		 	function(errorPayload){
		 		$log.info('errorPayload',errorPayload)				
		 	}
		);
	};

	$scope.loadMessage();

}