'use strict';

angular.module('giavacms-auth', [
        //'angular-jwt'
        //,'giavacms-localstorage'
    ])

    .run(function ($rootScope, AuthenticationService) {
        $rootScope.user = {};
        AuthenticationService.getUser().then(function (user) {
            $rootScope.user = user;
        });
    });
