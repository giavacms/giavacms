'use strict';

angular.module('giavacms-catalogue')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'catalogueconfiguration', {
            // set the url of this page
            url: '/catalogueconfiguration',
            // set the html template to show on this page
            templateUrl: 'app/catalogueconfiguration/catalogueconfiguration.html',
            // set the controller to load for this page
            controller: 'CatalogueconfigurationController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "catalogueconfiguration", 'Manage Catalogue Configuration', 70, 'fa fa-cog', APP.CATALOGUE.toggle);
    })

    .controller('CatalogueconfigurationController', function ($filter, $log, $mdDialog, $q, $scope, $state, APP, CatalogueconfigurationService) {

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

        RsListController($filter, $log, $mdDialog, $q, $scope, $state, APP, CatalogueconfigurationService, headers, overrides);

    });
;
