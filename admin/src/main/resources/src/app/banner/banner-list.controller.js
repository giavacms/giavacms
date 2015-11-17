'use strict';

angular.module('giavacms-banner')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'banner', {
            // set the url of this page
            url: '/banner',
            // set the html template to show on this page
            templateUrl: 'app/banner/banner-list.html',
            // set the controller to load for this page
            controller: 'BannerListController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "banner", 'Banner list', 20, 'fa fa-list', APP.BANNER.toggle);
    })

    .controller('BannerListController', function ($filter, $log, $mdDialog, $q, $scope, $state, BannerService) {

        var headers = [
        //            {field: 'id', label: 'identificativo', sortable: true},
                    {field: 'name', label: 'name', sortable: true},
                    {field: 'description', label: 'description', sortable: false},
                ];
        var overrides = {
            pageSize: 5
        };

        RsListController($filter, $log, $mdDialog, $q, $scope, $state, BannerService, headers, overrides);

    });
;
