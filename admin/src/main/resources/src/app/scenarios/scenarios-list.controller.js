'use strict';

angular.module('giavacms-scenario')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'scenarios_list', {
            // set the url of this page
            url: '/scenarios',
            // set the html template to show on this page
            templateUrl: 'app/scenarios/scenarios-list.html',
            // set the controller to load for this page
            controller: 'ScenariosListController'
        });

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'scenarios_view', {
            // set the url of this page
            url: '/scenarios',
            // set the html template to show on this page
            templateUrl: 'app/scenarios/scenarios-list.html',
            // set the controller to load for this page
            controller: 'ScenariosListController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "scenarios_list", 'Scenario list', 50, 'fa fa-list', APP.SCENARIO.toggle);
    })

    .controller('ScenariosListController', function ($filter, $log, $mdDialog, $q, $scope, $state, APP, ScenariosService, CategoriesService) {

        var headers = [
        //            {field: 'id', label: 'identificativo', sortable: true},
            {field: 'name', label: 'name', sortable: true},
            {field: 'description', label: 'desription', sortable: true},
        ];

        var overrides = {
            pageSize: 5
        };

        RsListController($filter, $log, $mdDialog, $q, $scope, $state, APP, ScenariosService, headers, overrides);

    });
;
