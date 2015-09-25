'use strict';

/**
 * @ngdoc function
 * @name LoginCtrl
 * @module triAngularAuthentication
 * @kind function
 *
 * @description
 *
 * Handles login form submission and response
 */
angular.module('login')

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

    .controller('LoginController', function ($log, $mdDialog, $scope, $state, APP, AuthenticationService, HOME, LOGIN) {

        $scope.appName = APP.name;

        // create blank user variable for login form
        $scope.user = {
            username: '',
            password: ''
        };


        // controller to handle login check
        $scope.loginClick = function () {
            $scope.loginInProgress = true;
            $scope.loginFailed = false;
            //$state.go('admin-panel.default.introduction');
            AuthenticationService.login($scope.user);
        };

        $scope.logout = function () {
            $mdDialog.show(
                $mdDialog.confirm()
                    .title('Confermi il logout?')
                    .content('I dati non salvati saranno eliminati')
                    .ok('Ok')
                    .cancel('Annulla')
            ).then(
                function (unused_ok) {
                    $log.debug('You are sure');
                    AuthenticationService.logout();
                },
                function (unused_cancel) {
                    $log.debug('You are not sure');
                }
            );
        };

        $scope.exitFromApp = function (path) {
            //ionic.Platform.exitApp();
        }

        $scope.$on('login-confirmed', function () {
            $log.debug('login-confirmed');
            $state.go(HOME, {}, {reload: true, inherit: false});

        });

        $scope.$on('login-failed', function (e, status) {
            $scope.loginInProgress = false;
            $scope.loginFailed = true;
        });

        $scope.$on('logout-complete', function () {
            $log.debug('logout-complete');
            $state.go(LOGIN);
        });

        $scope.fullname = AuthenticationService.getFullname();
        $scope.username = AuthenticationService.getUsername();
        $scope.roles = AuthenticationService.getRoles();

    });
