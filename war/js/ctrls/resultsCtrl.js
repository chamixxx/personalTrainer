angular.module('trainerApp').controller('resultsCtrl',resultsCtrlFnt);

resultsCtrlFnt.$inject=['$scope', '$log', '$window', '$cookies', 'comm', 'FeedService'];

function resultsCtrlFnt($scope, $log, $window, $cookies, comm, Feed) {
	$scope.keyWord = $cookies.get('keyWord');
	$scope.keyWord2 = "";
	$scope.feedSrc = 'http://www.bodybuilding.com/rss/articles'

	$scope.loadFeed=function(){        
        Feed.parseFeed($scope.feedSrc).then(function(res){
        	console.log("hey");
            $scope.feeds=res.data.responseData.feed.entries;
                    console.log($scope.feeds);

        });

    }

    $scope.loadFeed();

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