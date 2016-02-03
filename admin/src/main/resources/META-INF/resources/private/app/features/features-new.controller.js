'use strict';

angular.module('giavacms-catalogue')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'features_new', {
            // set the url of this page
            url: '/features-new',
            // set the html template to show on this page
            templateUrl: 'app/features/features-new.html',
            // set the controller to load for this page
            controller: 'FeaturesNewController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "features_new", 'Add Feature', 20, 'fa fa-plus', APP.CATALOGUE.toggle);
    })

    .controller('FeaturesNewController', function ($filter, $log, $mdDialog, $q, $scope, $state, $stateParams, APP, FeaturesService) {

        $scope.element = {};

        var overrides = {
        };

        var editFunction = RsEditController($log, $mdDialog, $q, $scope, $state, $stateParams, APP, FeaturesService, overrides);

        editFunction.init();

    });
;
