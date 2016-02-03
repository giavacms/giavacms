'use strict';

angular.module('giavacms-security')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'roles_list', {
            // set the url of this page
            url: '/roles',
            // set the html template to show on this page
            templateUrl: 'app/roles/roles-list.html',
            // set the controller to load for this page
            controller: 'RolesListController'
        });

    })

    .run(function(MenuService, APP) {
        // MenuService.addLink(APP.BASE + "roles_list", 'Role list', 10, 'fa fa-list', APP.ROLES.toggle);
        MenuService.addLinkWithACL('ACCOUNTS', APP.BASE + "roles_list", 'ROLES', 100, 'fa fa-legal');
    })

    .controller('RolesListController', function ($log, $scope, APP) {

        $scope.acls = APP.ACL;

        $scope.headers = [
            {field: 'acl', label: 'acl', sortable: true},
            {field: 'roles', label: 'roles', sortable: false},
        ];

        $scope.noOpHeader = true;

    });
;
