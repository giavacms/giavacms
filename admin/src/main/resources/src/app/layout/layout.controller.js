'use strict';

angular.module('giavacms-layout')

.controller('LayoutController', function($scope, AuthenticationService) {
    
    $scope.permit = AuthenticationService.permit;

});