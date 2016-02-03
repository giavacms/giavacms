'use strict';

angular.module('giavacms-login')

    .controller('LoginController', function ($location, $log, $mdDialog, $scope, $state, APP, AuthenticationService, MenuService, LOGIN) {

        $scope.appName = APP.NAME;
        $scope.appLogo = APP.LOGO;

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
            $location.path('/');
            // $state.go(HOME, {}, {reload: true, inherit: false});
            MenuService.recheck();
        });

        $scope.$on('login-failed', function (e, status) {
            $scope.loginInProgress = false;
            $scope.loginFailed = true;
            delete $scope.fullname;
            delete $scope.username;
            delete $scope.roles;
        });

        $scope.$on('logout-complete', function () {
            $log.debug('logout-complete');
            $state.go(LOGIN);
            delete $scope.fullname;
            delete $scope.username;
            delete $scope.roles;
        });

        $scope.fullname = AuthenticationService.getFullname();
        $scope.username = AuthenticationService.getUsername();
        $scope.roles = AuthenticationService.getRoles();

    });
