'use strict';

angular.module('giavacms-security')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'users_list', {
            // set the url of this page
            url: '/users',
            // set the html template to show on this page
            templateUrl: 'app/users/users-list.html',
            // set the controller to load for this page
            controller: 'UsersListController'
        });

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'users_view', {
            // set the url of this page
            url: '/users',
            // set the html template to show on this page
            templateUrl: 'app/users/users-list.html',
            // set the controller to load for this page
            controller: 'UsersListController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "users_list", 'User list', 10, 'fa fa-list', APP.USERS.toggle);
    })

    .controller('UsersListController', function ($filter, $log, $mdDialog, $q, $scope, $state, APP, UsersService) {

        var headers = [
//            {field: 'id', label: 'identificativo', sortable: true},
            {field: 'name', label: 'name', sortable: true},
            {field: 'username', label: 'login', sortable: true},
            {field: 'rolenames', label: 'roles', sortable: false},
        ];
        var overrides = {
            pageSize: 5
        };

        RsListController($filter, $log, $mdDialog, $q, $scope, $state, APP, UsersService, headers, overrides);

    });
;
