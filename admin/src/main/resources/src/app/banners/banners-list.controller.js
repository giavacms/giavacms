'use strict';

angular.module('giavacms-banners')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'banners_list', {
            // set the url of this page
            url: '/banners',
            // set the html template to show on this page
            templateUrl: 'app/banners/banners-list.html',
            // set the controller to load for this page
            controller: 'BannersListController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "banners_list", 'Banner list', 30, 'fa fa-list', APP.BANNERS.toggle);
    })

    .controller('BannersListController', function ($filter, $log, $mdDialog, $q, $scope, $state, APP, BannersService, BannertypesService) {

        var headers = [
        //            {field: 'id', label: 'identificativo', sortable: true},
            {field: 'name', label: 'name', sortable: true},
            {field: 'bannerType', label: 'banner type', sortable: false},
            //{field: 'description', label: 'description', sortable: false},
            {field: 'description', label: 'preview', sortable: false},
        ];

        var overrides = {
            pageSize: 5
        };

        RsListController($filter, $log, $mdDialog, $q, $scope, $state, APP, BannersService, headers, overrides);

        $scope.bannerTypes = [];

        var initBannerTypes = function () {
            return BannertypesService.getList({}, 0, 0).then(
                function (bannerTypes) {
                    $scope.bannerTypes = bannerTypes;
                    return $q.when(true);
                }
            );
        }

        initBannerTypes();

    });
;
