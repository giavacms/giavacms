'use strict';

angular.module('giavacms-resources')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'resources', {
            // set the url of this page
            url: '/resources',
            // set the html template to show on this page
            templateUrl: 'app/resources/resources-list.html',
            // set the controller to load for this page
            controller: 'ResourcesListController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "resources", 'Resource list', 1, 'fa fa-list', APP.RESOURCES.toggle);
    })

    .controller('ResourcesListController', function ($scope) {

    })

;
