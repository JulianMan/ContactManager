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
	
	// Set Person ID to Id from request handler - used in GET & POST requests
	$scope.personId = id;
	
	$http.get('PersonServlet/' + id).success(function(data){
		$scope.person = data;
		
		// Assign all attributes to new variable for easier access
		$scope.attributes = data.attributes;
		
		// Get primary contact details from attributes (Email, Phone, Picture)
		$scope.email = data.attributes.Email;
		$scope.phone = data.attributes.Phone;
		$scope.photo = data.attributes._photo;
		
		// Delete email, phone, and photo from attributes so they don't show up as attribute boxes
		delete $scope.person.attributes.Email;
		delete $scope.person.attributes.Phone;
		delete $scope.person.attributes._photo;
		
		//console.log (data.attributes);
		
		
		
		$scope.leftLimit = Math.ceil(Object.keys(data.attributes).length/2);
		$scope.rightLimit = Math.floor(Object.keys(data.attributes).length/2) + $scope.leftLimit;
		
		// Contact Form initial values - show edit button, hide edit form
		$scope.editContact = false;
		$scope.editButton = true;

	});
	
	// Function to add a new attribute to a specific contact
	$scope.addAttribute = function (heading, content) {
		var text = {  
	        "attributeName":heading,
	        "attributeValue":content,
	        "id":-1
		};
		
		// Append new attribute to attributes object with Object name 'heading'
		$scope.attributes[heading]=text;
		
		// Clear scope heading and content variables so form is empty for adding next attribute
		$scope.heading = "";
		$scope.content = ""; 
		
		// Recalculate column distribution
		$scope.leftLimit = Math.ceil(Object.keys($scope.attributes).length/2);
		$scope.rightLimit = Math.floor(Object.keys($scope.attributes).length/2) + $scope.leftLimit;
		
		// POST the data which includes the new attribute to server to be saved. 
		$scope.postData();
	}
	
	// Function to add new load reminder to a specific contact
	$scope.addLoanAttribute = function (content) {
		$scope.addAttribute("Load Reminder", content);
	}
	
	// Function called with any "Submit" button press, used to POST entire contact to server
	$scope.postData = function () {
		
		// Add in email, phone, and picture back into "attributes" object (previously removed)
		$scope.attributes["Email"]=$scope.email;
		$scope.attributes["Phone"]=$scope.phone;
		$scope.attributes["_photo"]=$scope.photo;
		
		// Do POST with proper email, phone, photo format in JSON file
		var req = {
		  method: 'POST',
		  url: 'PersonServlet/' + id,
		  data: $scope.person
		}
		
		console.log($scope.person);
		
		$http(req).then(
			function(){
				console.log ("success");
				console.log($scope.person);
				
				// Delete email, phone, and photo from attributes so they don't show up as attribute boxes.
				delete $scope.person.attributes.Email;
				delete $scope.person.attributes.Phone;
				delete $scope.person.attributes._photo;
			}, 
			function(){
				console.log ("error");
			}
		);
		
	}
	
});