'use strict';

angular.module('giavacms-banners')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'bannertypes_new', {
            // set the url of this page
            url: '/bannertypes-new',
            // set the html template to show on this page
            templateUrl: 'app/bannertypes/bannertypes-new.html',
            // set the controller to load for this page
            controller: 'BannertypesNewController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "bannertypes_new", 'Add BannerType', 20, 'fa fa-plus', APP.BANNERS.toggle);
    })

    .controller('BannertypesNewController', function ($filter, $log, $mdDialog, $q, $scope, $state, $stateParams, APP, BannertypesService) {

        $scope.element = {};

        var overrides = {
        };

        var editFunction = RsEditController($log, $mdDialog, $q, $scope, $state, $stateParams, APP, BannertypesService, overrides);

        editFunction.init();

    });
;
