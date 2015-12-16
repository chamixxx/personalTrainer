angular.module('trainerApp').controller('addTrainingCtrl',addTrainingCtrlFnt);

addTrainingCtrlFnt.$inject=['$scope', '$log', '$window', 'comm'];

function addTrainingCtrlFnt($scope, $log, $window, comm){
	
	$scope.trainingPlan = {
		title:"",
		description:"",
		domain:"",
		minute:0,
		exercices:[]
	};

	$scope.currentEx = {
		title:"",
		description:"",
		minute:0
	};

	$scope.totaleTime=0;

	$scope.showConsole = function(){
		console.log($scope.trainingPlan);
	};

	$scope.addExercice = function() {
		var exercice = {
			title:$scope.currentEx.title,
			description:$scope.currentEx.description,
			minute:$scope.currentEx.minute
		};
		$scope.totaleTime = $scope.totaleTime + exercice.minute;
		$scope.trainingPlan.exercices.push(exercice);
	};

	$scope.deleteExercice = function(exercice) {
		var index = $scope.trainingPlan.exercices.indexOf(exercice);
		$scope.totaleTime = $scope.totaleTime - exercice.minute;
      	$scope.trainingPlan.exercices.splice(index,1);
	};

	$scope.sendTraining = function() {
		$scope.trainingPlan.minute = $scope.totaleTime;
		var future = comm.postTrainingPlan($scope.trainingPlan);
		 future.then(
		 	function(payload) {
		 		$log.info('payload',payload);
		 		alert("trainingPlan Added");
		 	},
		 	function(errorPayload){
		 		$log.info('errorPayload',errorPayload)				
		 	}
		);
	};

	$scope.goBack = function () {
    	$window.location.assign("/ha-search-screen.html");
    }
}