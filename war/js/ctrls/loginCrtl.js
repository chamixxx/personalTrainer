angular.module('trainerApp').controller('loginCtrl',loginCtrlFnt);

loginCtrlFnt.$inject=['$scope', '$log', '$window', '$cookies', 'comm'];

function loginCtrlFnt($scope, $log, $window, $cookies, comm){

	$scope.isSignedIn = false;
	$scope.userName = "";
	$scope.email = "";


	function onSignIn(googleUser) {
	$scope.isSignedIn = true;
    var profile = googleUser.getBasicProfile();

    console.log('ID: ' + profile.getId());
    console.log('Name: ' + profile.getName());
    console.log('Image URL: ' + profile.getImageUrl());
    console.log('Email: ' + profile.getEmail());

    $cookies.put('userId', profile.getId());

    var id_token = googleUser.getAuthResponse().id_token;

    var future = comm.postToken(id_token);
		 future.then(
		 	function(payload) {
		 		$log.info('payload',payload);
		 		 $scope.loadProfilInfo();
		 	},
		 	function(errorPayload){
		 		$log.info('errorPayload',errorPayload)				
		 	}
		);
		
   };

   window.onSignIn = onSignIn;

   $scope.loadProfilInfo=function() {
		
		 var future = comm.getUserInfo();
		 future.then(
		 	function(payload) {
		 		$log.info('payload',payload);
		 		$scope.userName = payload.name;
				$scope.email = payload.mail;
		 	},
		 	function(errorPayload){
		 		$log.info('errorPayload',errorPayload)				
		 	}
		);
	};

	$scope.signOut = function () {
		$scope.isSignedIn = false;
    	var auth2 = gapi.auth2.getAuthInstance();
    	auth2.signOut().then(function () {
      		console.log('User signed out.');
    	});
    };

    $scope.openStats = function() {
    	$window.location.assign("/statistics.html");
    }
}