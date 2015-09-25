'use strict';

angular.module('giavacms-login', ['giavacms-auth','ui.router'])

    .constant('LOGIN', 'authentication.login')

    .config(function ($stateProvider, LOGIN) {

        $stateProvider
            .state('authentication', {
                abstract: true,
                templateUrl: 'app/login/authentication.html'
            })
            .state(LOGIN, {
                url: '/login',
                templateUrl: 'app/login/login.html',
                controller: 'LoginController'
            })
        ;
    })
