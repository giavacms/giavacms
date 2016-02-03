'use strict';

angular.module('giavacms-catalogue')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'categories_list', {
            // set the url of this page
            url: '/categories',
            // set the html template to show on this page
            templateUrl: 'app/categories/categories-list.html',
            // set the controller to load for this page
            controller: 'CategoriesListController'
        });

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'categories_view', {
            // set the url of this page
            url: '/categories',
            // set the html template to show on this page
            templateUrl: 'app/categories/categories-list.html',
            // set the controller to load for this page
            controller: 'CategoriesListController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "categories_list", 'Category list', 30, 'fa fa-list', APP.CATALOGUE.toggle);
    })

    .controller('CategoriesListController', function ($filter, $log, $mdDialog, $q, $scope, $state, APP, CategoriesService) {

        var headers = [
//            {field: 'id', label: 'identificativo', sortable: true},
            {field: 'name', label: 'name', sortable: true},
            {field: 'description', label: 'description', sortable: true},
        ];
        var overrides = {
            pageSize: 5
        };

        RsListController($filter, $log, $mdDialog, $q, $scope, $state, APP, CategoriesService, headers, overrides);

    });
;
