angular.module('trainerApp').controller('resultsCtrl',resultsCtrlFnt);

resultsCtrlFnt.$inject=['$scope', '$log', '$window', '$cookies', 'comm'];

function resultsCtrlFnt($scope, $log, $window, $cookies, comm) {
	$scope.keyWord = $cookies.get('keyWord');
	$scope.keyWord2 = "";


    $scope.loadFeedUrlFetch=function(){        
        var future = comm.getRSS();
		future.then(
		 	function(payload) {
		 		$log.info('rss',payload);
		 		$scope.feeds = payload.rss.channel.item;
		 	},
		 	function(errorPayload){
		 		$log.info('errorPayload',errorPayload)				
		 	}
		);
    };


    $scope.loadFeedUrlFetch();

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
	 		$log.info('errorPayloadSearch',errorPayload)				
	 	}
	);	
    };

    $scope.goToDetailsTrainings = function(name) {
    	$cookies.put('detailsName', name);
    	$cookies.put('type', "TrainingPlan");
    	$cookies.put('parentKey',"-1");
    	$window.location.assign("/ha-result-detail-screen.html");
    } 

    $scope.goToDetailsExercices = function(name,parentKey) {
    	$cookies.put('detailsName', name);
    	$cookies.put('type', "Exercices");
    	$cookies.put('parentKey',parentKey);
    	$window.location.assign("/ha-result-detail-screen.html");
    }

}