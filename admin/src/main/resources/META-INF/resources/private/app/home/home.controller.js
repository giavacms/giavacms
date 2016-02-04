'use strict';

angular.module('giavacms-home')

    .controller('HomeController', function ($scope, AuthenticationService) {
        $scope.user = {};
        AuthenticationService.getUser().then(function (user) {
            $scope.user = user;
        });
    })

;
