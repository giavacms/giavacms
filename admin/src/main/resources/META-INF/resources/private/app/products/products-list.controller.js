'use strict';

angular.module('giavacms-catalogue')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'products_list', {
            // set the url of this page
            url: '/products',
            // set the html template to show on this page
            templateUrl: 'app/products/products-list.html',
            // set the controller to load for this page
            controller: 'ProductsListController'
        });

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'products_view', {
            // set the url of this page
            url: '/products',
            // set the html template to show on this page
            templateUrl: 'app/products/products-list.html',
            // set the controller to load for this page
            controller: 'ProductsListController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "products_list", 'Product list', 50, 'fa fa-list', APP.CATALOGUE.toggle);
    })

    .controller('ProductsListController', function ($filter, $log, $mdDialog, $q, $scope, $state, APP, ProductsService, CategoriesService) {

        var headers = [
        //            {field: 'id', label: 'identificativo', sortable: true},
            {field: 'name', label: 'name', sortable: true},
            {field: 'code', label: 'code', sortable: true},
            {field: 'category.name', label: 'category', sortable: false},
        ];

        var overrides = {
            pageSize: 5
        };

        RsListController($filter, $log, $mdDialog, $q, $scope, $state, APP, ProductsService, headers, overrides);

        $scope.richContentTypes = [];

        var initCategories = function () {
            return CategoriesService.getList({}, 0, 0).then(
                function (categories) {
                    $scope.categories = categories;
                    return $q.when(true);
                }
            );
        }

        initCategories();

    });
;
