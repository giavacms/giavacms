'use strict';

angular.module('giavacms-richcontents')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'richcontenttypes_list', {
            // set the url of this page
            url: '/richcontenttypes',
            // set the html template to show on this page
            templateUrl: 'app/richcontenttypes/richcontenttypes-list.html',
            // set the controller to load for this page
            controller: 'RichcontenttypesListController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "richcontenttypes_list", 'RichContentType list', 10, 'fa fa-list', APP.RICHCONTENTS.toggle);
    })

    .controller('RichcontenttypesListController', function ($filter, $log, $mdDialog, $q, $scope, $state, APP, RichcontenttypesService) {

        var headers = [
//            {field: 'id', label: 'identificativo', sortable: true},
            {field: 'name', label: 'name', sortable: true},
        ];
        var overrides = {
            pageSize: 5
        };

        RsListController($filter, $log, $mdDialog, $q, $scope, $state, APP, RichcontenttypesService, headers, overrides);

    });
;
