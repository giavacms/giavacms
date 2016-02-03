'use strict';

angular.module('giavacms-catalogue')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'products_new', {
            // set the url of this page
            url: '/products-new',
            // set the html template to show on this page
            templateUrl: 'app/products/products-edit.html',
            // set the controller to load for this page
            controller: 'ProductsEditController'
        });

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'products_edit', {
            // set the url of this page
            url: '/products-edit/:id',
            // set the html template to show on this page
            templateUrl: 'app/products/products-edit.html',
            // set the controller to load for this page
            controller: 'ProductsEditController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "products_new", 'Add Product', 60, 'fa fa-plus', APP.CATALOGUE.toggle);
    })

    .controller('ProductsEditController', function ($filter, $log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, ProductsService, CategoriesService, RsResource) {

        var confirmAndGoTo = function(toState, skipConfirm) {
            var confirm = null;
            if (skipConfirm) {
                confirm = $q.when(true);
            }
            else {
                confirm = $mdDialog.show(
                    $mdDialog.confirm()
                        .title('Attenzione')
                        .content('Eventuali modifiche non saranno salvate?')
                        .ok('Ok')
                        .cancel('Annulla')
                );
            }
            confirm.then(
                function ok() {
                    $log.debug('You are sure');
                    $state.go(toState, {id: $scope.element.id});
                },
                function cancel() {
                    $log.debug('You are not sure');
                }
            );
        }

        $scope.editDocuments = function(skipConfirm) {
            confirmAndGoTo(APP.BASE + 'products_edit_documents', skipConfirm);
        }

        $scope.editImages = function(skipConfirm) {
            confirmAndGoTo(APP.BASE + 'products_edit_images', skipConfirm);
        }

        var overrides = {
            postSave: function (ok,element) {
                if (ok) {
                    $scope.element = element;
                    $scope.editDocuments(true);
                }
                else {
                    $mdDialog.show(
                        $mdDialog.alert().title('Errore').content('Salvataggio non riuscito').ok('Ok'));
                }
            },
            postUpdate: function (ok) {
                if (ok) {
                    $scope.editDocuments(true);
                }
                else {
                    $mdDialog.show(
                        $mdDialog.alert().title('Errore').content('Salvataggio delle modifiche non riuscito').ok('Ok'));
                }
            }

        };

        var editFunction = RsEditController($log, $mdDialog, $q, $scope, $state, $stateParams, APP, ProductsService, overrides)

        if ( !$scope.element ) {
            $scope.element = {};
        }

        if ( !$scope.element.category ) {
            // ng-select riempie tutto l'oggetto. se lo inizializzo non funziona il controllo required
            //$scope.element.bannerType = {};
        }

        $scope.categories = [];

        var initCategories = function () {
            return CategoriesService.getList({}, 0, 0).then(
                function (categories) {
                    $scope.categories = categories;
                    categories.forEach(function (category) {
                            if ($scope.element.category && $scope.element.category.id == category.id) {
                                $scope.element.category = category;
                            }
                        }
                    );
                    return $q.when(true);
                }
            );
        }

        editFunction.init().then(
            function(result) {
                if ( result ) {
                    initCategories();
                 }
            }
        );

    });
;
