'use strict';

angular.module('giavacms-banner')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'bannertypes_new', {
            // set the url of this page
            url: '/bannertype-new',
            // set the html template to show on this page
            templateUrl: 'app/bannertype/bannertype-new.html',
            // set the controller to load for this page
            controller: 'BannertypeNewController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "bannertypes_new", 'Add BannerType', 20, 'fa fa-plus', APP.BANNER.toggle);
    })

    .controller('BannertypeNewController', function ($filter, $log, $mdDialog, $q, $scope, $state, $stateParams, APP, BannertypeService) {

        $scope.element = {};

        var overrides = {
        };

        RsEditController($log, $mdDialog, $q, $scope, $state, $stateParams, APP, BannertypeService, overrides);

    });
;
