'use strict';

angular.module('giavacms-richcontents')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'richcontenttypes_new', {
            // set the url of this page
            url: '/richcontenttypes-new',
            // set the html template to show on this page
            templateUrl: 'app/richcontenttypes/richcontenttypes-new.html',
            // set the controller to load for this page
            controller: 'RichcontenttypesNewController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "richcontenttypes_new", 'Add RichContentType', 20, 'fa fa-plus', APP.RICHCONTENTS.toggle);
    })

    .controller('RichcontenttypesNewController', function ($filter, $log, $mdDialog, $q, $scope, $state, $stateParams, APP, RichcontenttypesService) {

        $scope.element = {};

        var overrides = {
        };

        var editFunction = RsEditController($log, $mdDialog, $q, $scope, $state, $stateParams, APP, RichcontenttypesService, overrides);

        editFunction.init();

    });
;
