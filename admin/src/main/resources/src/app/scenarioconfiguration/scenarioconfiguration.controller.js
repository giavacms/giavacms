'use strict';

angular.module('giavacms-scenario')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'scenarioconfiguration', {
            // set the url of this page
            url: '/scenarioconfiguration',
            // set the html template to show on this page
            templateUrl: 'app/scenarioconfiguration/scenarioconfiguration.html',
            // set the controller to load for this page
            controller: 'ScenarioconfigurationController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "scenarioconfiguration", 'Manage Configuration', 70, 'fa fa-cog', APP.SCENARIO.toggle);
    })

    .controller('ScenarioconfigurationController', function ($filter, $log, $mdDialog, $q, $scope, $state, APP, ScenarioconfigurationService) {

        var headers = [
//            {field: 'id', label: 'identificativo', sortable: true},
            {field: 'resize', label: 'resize', sortable: false},
            {field: 'maxWidthOrHeight', label: 'max w/h', sortable: false},
            {field: 'withPrices', label: 'prices', sortable: false},
            {field: 'withDimensions', label: 'dimensions', sortable: false},
         ];
        var overrides = {
            pageSize: 1
        };

        RsListController($filter, $log, $mdDialog, $q, $scope, $state, APP, ScenarioconfigurationService, headers, overrides);

    });
;
