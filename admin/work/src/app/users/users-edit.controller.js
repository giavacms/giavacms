'use strict';

angular.module('giavacms-security')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'users_new', {
            // set the url of this page
            url: '/users-new',
            // set the html template to show on this page
            templateUrl: 'app/users/users-edit.html',
            // set the controller to load for this page
            controller: 'UsersEditController'
        });

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'users_edit', {
            // set the url of this page
            url: '/users-edit/:id',
            // set the html template to show on this page
            templateUrl: 'app/users/users-edit.html',
            // set the controller to load for this page
            controller: 'UsersEditController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "users_new", 'Add User', 20, 'fa fa-plus', APP.USERS.toggle);
    })

    .controller('UsersEditController', function ($filter, $log, $mdDialog, $q, $scope, $state, $stateParams, APP, UsersService) {

        $scope.element = {};

        var overrides = {
        };

        var editFunction = RsEditController($log, $mdDialog, $q, $scope, $state, $stateParams, APP, UsersService, overrides);

        $scope.roleMap = {};
        Object.keys(APP.ACL).forEach(
            function(aclName) {
                var roles = APP.ACL[aclName].split(',');
                for ( var r = 0 ; r < roles.length ; r++ ) {
                    $scope.roleMap[roles[r].trim()] = false;
                }
            }
        );

        editFunction.init().then(
            function() {
                if ( ! $scope.element ) {
                    $scope.element = { };
                }
                if ( ! $scope.element.roleNames ) {
                    $scope.element.roleNames = '';
                }
                var actualRoles = [];
                var currentRoles = $scope.element.roleNames.split(',');
                for ( var r = 0 ; r < currentRoles.length ; r++ ) {
                    if ( angular.isDefined($scope.roleMap[currentRoles[r].trim()]) ) {
                        actualRoles.push(currentRoles[r].trim());
                        $scope.roleMap[currentRoles[r].trim()] = true;
                    }
                }
                $scope.element.roles = actualRoles;
                delete $scope.element.roleNames;
            }
        );

        $scope.toggle = function(role) {
            var newRoles = [];
            for ( var r = 0 ; r < $scope.element.roles.length; r++ ) {
                newRoles.push($scope.element.roles[r]);
            }
            for ( var r = 0 ; r < newRoles.length; r++ ) {
                if ( newRoles[r] == role ) {
                    newRoles.splice(r,1);
                    $scope.roleMap[role] = false;
                    $scope.element.roles = newRoles;
                    return;
                }
            }
            newRoles.push(role);
            $scope.element.roles = newRoles;
            $scope.roleMap[role] = true;
        }


        $scope.all = function() {
            var allRoles = [];
            Object.keys($scope.roleMap).forEach(
                function(roleName) {
                    $scope.roleMap[roleName] = true;
                    if ( allRoles.indexOf(roleName) < 0 ) {
                        allRoles.push(roleName);
                    }
                }
            );
            $scope.element.roles = allRoles;
        }

        $scope.none = function() {
            $scope.element.roles = [];
            Object.keys($scope.roleMap).forEach(
                function(roleName) {
                    $scope.roleMap[roleName] = false;
                }
            );
        }

    });
;
