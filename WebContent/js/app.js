'use strict';

/* App Module */
// Array lists modules that phonecatAPP depends on
var contactManagerApp = angular.module('contactManagerApp', [
  'ngRoute',
  'contactManagerControllers'
]);

//var app = angular.module('App', ['ui.router','ui.bootstrap']);

contactManagerApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/contact', {
        templateUrl: 'partials/contact-list.html',
        controller: 'ContactListCtrl'
      }).
      when('/contact/:personId', {
        templateUrl: 'partials/contact-detail.html',
        controller: 'ContactDetailCtrl'
      }).
      when('/reminders', {
          templateUrl: 'partials/reminders.html',
          controller: 'ReminderCtrl'
        }).
      otherwise({
      	//browser address doesn't match either route
        redirectTo: '/contact'
      });
}]);