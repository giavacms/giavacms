'use strict';

angular.module('giavacms-catalogue')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'categories_new', {
            // set the url of this page
            url: '/categories-new',
            // set the html template to show on this page
            templateUrl: 'app/categories/categories-edit.html',
            // set the controller to load for this page
            controller: 'CategoriesEditController'
        });

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'categories_edit', {
            // set the url of this page
            url: '/categories-edit/:id',
            // set the html template to show on this page
            templateUrl: 'app/categories/categories-edit.html',
            // set the controller to load for this page
            controller: 'CategoriesEditController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "categories_new", 'Add Category', 40, 'fa fa-plus', APP.CATALOGUE.toggle);
    })

    .controller('CategoriesEditController', function ($filter, $log, $mdDialog, $q, $scope, $state, $stateParams, APP, CategoriesService, FeaturesService) {

        $scope.element = { };

        var overrides = {
        };

        var editFunction = RsEditController($log, $mdDialog, $q, $scope, $state, $stateParams, APP, CategoriesService, overrides);

        editFunction.init();

        $scope.clean = function (index) {
            delete $scope.element['prop'+index];
            delete $scope.element['ref'+index];
        }

        $scope.cleanRef = function (index) {
            delete $scope.element['ref'+index];
        }

        $scope.choose = function (index) {
            $mdDialog.show({
                controller: function($log, $mdDialog, $q, $sce, $scope, APP, RsResource) {
                    var headers = [
                        {field: 'name', label: 'name', sortable: false},
                    ];
                    var overrides = {
                        pageSize: 1000,
                        postList: function(data) {
                            var map = {};
                            for ( var d = 0 ; d < data.length ; d++ ) {
                                var distinct = map[data[d].name];
                                if ( ! distinct ) {
                                    map[data[d].name] = data[d];
                                }
                                else {
                                    distinct.option += ',' + data[d].option;
                                }
                            }
                            data.splice(0,data.length);
                            Object.keys(map).forEach(
                                function(key) {
                                    data.push( map[key] );
                                }
                            );
                        }
                    };
                    RsListController($filter, $log, $mdDialog, $q, $scope, $state, APP, FeaturesService, headers, overrides);
                    $scope.pick = function(resource) {
                        $mdDialog.hide(resource);
                    }
                    $scope.cancel = function () {
                        $mdDialog.hide();
                    }
                },
                templateUrl: 'app/categories/dialogs/features-list-dialog.html'
            }).then(function (feature) {
                if (feature) {
                    $scope.element['prop'+index] = feature.name;
                    $scope.element['ref'+index] = feature.option;
                }
            }, function () {
                $log.debug('no selection');
            })
        };

    });
;
