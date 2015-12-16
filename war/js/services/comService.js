angular.module('comApp', []).service('comm',commFnc);
commFnc.$inject = ['$log', '$http', '$q'];

function commFnc($log, $http, $q) {
	
	var fncContainer={
		getMessage:getMessage,
		postToken:postToken,
		getUserInfo:getUserInfo,
		postTrainingPlan:postTrainingPlan,
		getTrainings:getTrainings,
		getSearchResults:getSearchResults,
		getRSS:getRSS
	};

	function getMessage() { 
		var deferred = $q.defer();
		$http.get('http://localhost:8888/loadWelcome').
			success(function(data, status, headers, config) {
			 	deferred.resolve(data)
			}).
			error(function(data, status, headers, config) {
				deferred.reject(status);
			});
        return deferred.promise;
	};

	function postToken(token) {
		var deferred = $q.defer();
		var req = {
 				method: 'POST',
 				url: '/googleAuth',
 				headers: {
   					'Content-Type': 'application/x-www-form-urlencoded'
 				},
 				data: { idtoken: token }
		}

		$http(req).success(function(data, status, headers, config) {
				console.log(data);
			 	deferred.resolve(data);
			}).
			error(function(data, status, headers, config) {
				deferred.reject(status);
			});
		return deferred.promise;
	};

	function getUserInfo() { 
		var deferred = $q.defer();
		$http.get('/googleAuth').
			success(function(data, status, headers, config) {
			 	deferred.resolve(data)
			}).
			error(function(data, status, headers, config) {
				deferred.reject(status);
			});
        return deferred.promise;
	};

	function postTrainingPlan(training) {
		var deferred = $q.defer();
		var req = {
 				method: 'POST',
 				url: '/addTraining',
 				headers: {
   					'Content-Type': 'application/json'
 				},
 				data : JSON.stringify(training)
		}

		$http(req).success(function(data, status, headers, config) {
				console.log(data);
			 	deferred.resolve(data);
			}).
			error(function(data, status, headers, config) {
				deferred.reject(status);
			});
		return deferred.promise;
	};

	function getTrainings() { 
		var deferred = $q.defer();
		$http.get('/getTrainings').
			success(function(data, status, headers, config) {
			 	deferred.resolve(data)
			}).
			error(function(data, status, headers, config) {
				deferred.reject(status);
			});
        return deferred.promise;
	};

	function getSearchResults(keyWord) { 
		var req = {
	    		url: '/getSearchResults', 
			    method: "GET",
			    params: {"keyWord":keyWord}
 			}

		var deferred = $q.defer();
		$http(req).
			success(function(data, status, headers, config) {
			 	deferred.resolve(data)
			}).
			error(function(data, status, headers, config) {
				deferred.reject(status);
			});
        return deferred.promise;
	};

	function getRSS() { 
		var deferred = $q.defer();
		$http.get('/getRSS').
			success(function(data, status, headers, config) {
			 	deferred.resolve(data)
			}).
			error(function(data, status, headers, config) {
				deferred.reject(status);
			});
        return deferred.promise;
	};

	
	return fncContainer;
}