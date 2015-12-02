'use strict';

angular.module('giavacms-catalogue')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'features_list', {
            // set the url of this page
            url: '/features',
            // set the html template to show on this page
            templateUrl: 'app/features/features-list.html',
            // set the controller to load for this page
            controller: 'FeaturesListController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "features_list", 'Feature list', 10, 'fa fa-list', APP.CATALOGUE.toggle);
    })

    .controller('FeaturesListController', function ($filter, $log, $mdDialog, $q, $scope, $state, APP, FeaturesService) {

        var headers = [
//            {field: 'id', label: 'identificativo', sortable: true},
            {field: 'name', label: 'name', sortable: true},
            {field: 'option', label: 'option', sortable: true},
        ];
        var overrides = {
            pageSize: 5
        };

        RsListController($filter, $log, $mdDialog, $q, $scope, $state, APP, FeaturesService, headers, overrides);

    });
;
