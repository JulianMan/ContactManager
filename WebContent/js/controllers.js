'use strict';

/* Controllers */
var contactManagerControllers = angular.module('contactManagerControllers', []);

contactManagerControllers.controller('ContactListCtrl', function ($scope, $http) {
	$http.get('PersonServlet/all').success(function(data){
	//$http.get('JSON_test/contacts.json').success(function(data){
		$scope.contacts = data;
	});

	$scope.orderProp = 'name';
});

contactManagerControllers.controller('ContactDetailCtrl', function ($scope, $routeParams, $http) {
	
	var id = $routeParams.personId;
	$http.get('PersonServlet/' + id).success(function(data){
		$scope.person = data;
		$scope.quantity = 2;
		$scope.leftLimit = Math.ceil(Object.keys(data.attributes).length/2);
		$scope.rightLimit = Math.floor(Object.keys(data.attributes).length/2) + $scope.leftLimit;
		
		// Contact Form initial values - show edit button, hide edit form
		$scope.editContact = false;
		$scope.editButton = true;

	});
	
	$scope.personId = id;
	
});