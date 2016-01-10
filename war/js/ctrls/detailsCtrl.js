angular.module('trainerApp').controller('detailsCtrl',detailsCtrlFnt);

detailsCtrlFnt.$inject=['$scope', '$log', '$window', '$cookies', 'comm', '$filter'];

function detailsCtrlFnt($scope, $log, $window, $cookies, comm, $filter) {
	$scope.detailsName = $cookies.get('detailsName');
	$scope.detailsType = $cookies.get('type');
	$scope.parentKey = $cookies.get('parentKey');

	$scope.isPartial = false;

	if ($scope.parentKey == "-1") {
		$scope.isPartial = true;
	}

	console.log($scope.detailsName);
	console.log($scope.detailsType);
	console.log($scope.parentKey);

	$scope.finishedPrevious = [];
	$scope.allexerciceNotDone = true;

	var future = comm.getDetails($scope.detailsName,$scope.detailsType,$scope.parentKey);
	future.then(
	 	function(payload) {
	 		$scope.details = payload;
	 		$scope.exercices = payload.exercices;
	 		var i=1;
	 		angular.forEach(payload.exercices , function(value, key) {
				$scope.finishedPrevious[i]=true;
				i++;
	 		});
	 		$log.info('list',$scope.details);
	 	},
	 	function(errorPayload){
	 		$log.info('errorPayload',errorPayload)				
	 	}
	);



	$scope.play = function(index) {
		document.getElementsByTagName('timer')[index].start();	
	}
	$scope.pause = function(index) {
		document.getElementsByTagName('timer')[index].stop();	
	}
	$scope.reset = function(index) {
		document.getElementsByTagName('timer')[index].reset();	
	}

	$scope.validateExo = function(index) {
		if (index < $scope.exercices.length) {
			$scope.finishedPrevious[index+1]=false;
		}
		
		var exo;

		var currentDate = $filter('date')(new Date(), 'dd/MM/yyyy');
		var userId = $cookies.get('userId');
		var traingPlanName = $scope.details.title;
		var exoName = $scope.exercices[index].title;
		var completed = true;

		exo = {
			'currentDate':currentDate,
			'userId':userId,
			'traingPlanName':traingPlanName,
			'exoName':exoName,
			'completed':completed
		}

		var future = comm.updateExo(exo);
		future.then(
		 	function(payload) {
		 		$log.info('done',payload);
		 	},
		 	function(errorPayload){
		 		$log.info('errorPayload',errorPayload)				
		 	}
		);
	};

	$scope.unvalidateExo = function(index) {
		if (index < $scope.exercices.length) {
			$scope.finishedPrevious[index+1]=false;
		}
		
		var exo;

		var currentDate = $filter('date')(new Date(), 'dd/MM/yyyy');
		var userId = $cookies.get('userId');
		var traingPlanName = $scope.details.title;
		var exoName = $scope.exercices[index].title;
		var completed = false;

		exo = {
			'currentDate':currentDate,
			'userId':userId,
			'traingPlanName':traingPlanName,
			'exoName':exoName,
			'completed':completed
		}

		var future = comm.updateExo(exo);
		future.then(
		 	function(payload) {
		 		$log.info('done',payload);
		 	},
		 	function(errorPayload){
		 		$log.info('errorPayload',errorPayload)				
		 	}
		);
	}

	$scope.back = function() {
		$window.history.back();
	}
}