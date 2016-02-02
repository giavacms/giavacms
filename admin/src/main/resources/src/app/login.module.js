'use strict';

angular.module('giavacms-login', ['ui.router', 'giavacms-auth', 'giavacms-config'])

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

    .run(function ($rootScope, $state, AuthenticationService, LOGIN) {

        $rootScope.$on('$stateChangeStart', function (e, to) {
            if (to.name == LOGIN) {
                return;
            }
            AuthenticationService.isLogged().then(
                function (tokenPayload) {
                    if (!tokenPayload) {
                        e.preventDefault();
                        $state.go(LOGIN);
                    }
                }
            )
        })
    })

