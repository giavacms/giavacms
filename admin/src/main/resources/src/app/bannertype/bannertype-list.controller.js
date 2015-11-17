'use strict';

angular.module('giavacms-banner')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'bannertype', {
            // set the url of this page
            url: '/bannertype',
            // set the html template to show on this page
            templateUrl: 'app/bannertype/bannertype-list.html',
            // set the controller to load for this page
            controller: 'BannertypeListController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "bannertype", 'Bannertype list', 10, 'fa fa-list', APP.BANNER.toggle);
    })

    .controller('BannertypeListController', function ($filter, $log, $mdDialog, $q, $scope, $state, BannertypeService) {

        var headers = [
//            {field: 'id', label: 'identificativo', sortable: true},
            {field: 'name', label: 'name', sortable: true},
            {field: 'description', label: 'description', sortable: false},
        ];
        var overrides = {
            pageSize: 5
        };

        RsListController($filter, $log, $mdDialog, $q, $scope, $state, BannertypeService, headers, overrides);

    });
;
