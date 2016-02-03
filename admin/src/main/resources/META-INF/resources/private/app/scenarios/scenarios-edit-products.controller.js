'use strict';

angular.module('giavacms-scenario')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'scenarios_edit_products', {
            // set the url of this page
            url: '/scenarios-edit/:id/products',
            // set the html template to show on this page
            templateUrl: 'app/scenarios/scenarios-edit-products.html',
            // set the controller to load for this page
            controller: 'ScenariosEditProductsController'
        });

    })

    .controller('ScenariosEditProductsController', function ($filter, $http, $log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, RsResource, ScenariosService, ScenariosCategoriesService, ScenariosProductsService) {

        var imgPreview = function (product, image) {
            product.imgPreview = APP.PROTOCOL + "://" + APP.HOST + APP.CONTEXT + image.filename;
            return $q.when(true);
        }

        var preview = function (product) {
            var reqParams = {};
            reqParams['host'] = APP.HOST;
            reqParams['context'] = APP.CONTEXT;
            reqParams['entityType'] = 'products';
            reqParams['id'] = product.id;
            reqParams['entityType2'] = 'images';
            RsResource.query(reqParams,
                function (data) {
                    if ( data && data.length > 0 ) {
                        imgPreview(product, data[0]);
                    }
                },
                function(error) {
                    $log.error(error);
                }
            );
            return $q.when(true);
        }

        var initElement = function () {
            return ScenariosService.getElement($stateParams.id).then(
                function (element) {
                    if ( element && element.id && element.id == $stateParams.id ) {
                        $scope.element = element;
                        return $q.when(true);
                    }
                    else {
                        return $q.when(false);
                    }
                }
            );
        }

        var initProducts = function() {
            var reqParams = {};
            reqParams['host'] = APP.HOST;
            reqParams['context'] = APP.CONTEXT;
            reqParams['entityType'] = 'scenarios';
            reqParams['id'] = $stateParams.id;
            reqParams['entityType2'] = 'products';
            RsResource.query(reqParams,
                function (data) {
                    $scope.resources = data;
                    data.forEach(
                        function(r)  {
                            preview(r);
                        }
                    );
                },
                function(error) {
                    $log.error(error);
                }
            );
        };

        $scope.headers = [
            {field: 'category.name', label: 'category', sortable: false},
            {field: 'name', label: 'name', sortable: false},
            {field: 'preview', label: 'preview', sortable: false},
        ];

        initElement().then(
            function() {
                initProducts();
            }
        );

        $scope.delete = function (product, skipConfirm) {
            var confirm = null;
            if (skipConfirm) {
                confirm = $q.when(true);
            }
            else {
                confirm = $mdDialog.show(
                    $mdDialog.confirm()
                        .title('Conferma')
                        .content('Confermi l\'eliminazione di ' + product.name + ' ?')
                        .ok('Ok')
                        .cancel('Annulla')
                );
            }
            return confirm.then(
                function ok() {
                    $log.debug('You are sure');
                    var reqParams = {};
                    reqParams['host'] = APP.HOST;
                    reqParams['context'] = APP.CONTEXT;
                    reqParams['entityType'] = 'scenarios';
                    reqParams['id'] = $stateParams.id;
                    reqParams['entityType2'] = 'products';
                    reqParams['id2'] = product.id;
                    return RsResource.delete(reqParams,
                        function () {
                            return initProducts();
                        },
                        function () {
                            return initProducts();
                        }
                    );
                },
                function cancel() {
                    $log.debug('You are not sure');
                    return $q.when(false);
                }
            );
        }

        $scope.editDocuments = function() {
            $state.go(APP.BASE + 'scenarios_edit_documents', {id: $stateParams.id});
        }

        $scope.editImages = function() {
            $state.go(APP.BASE + 'scenarios_edit_images', {id: $stateParams.id});
        }

        $scope.editProducts = function(skipConfirm) {
            $state.go(APP.BASE + 'scenarios_edit_products', {id: $stateParams.id});
        }

        $scope.editElement = function(skipConfirm) {
            $state.go(APP.BASE + 'scenarios_edit', {id: $stateParams.id});
        }

        var embed = function(product) {
            var uploadUrl = APP.PROTOCOL + "://" + APP.HOST + APP.CONTEXT + '/api/v1/scenarios/' + $stateParams.id + '/products/' + product.id;
            $http.post(uploadUrl)
                .success(function(){
                    initProducts();
                })
                .error(function(){
                    $mdDialog.show(
                        $mdDialog.alert().title('Errore').content('Salvataggio non riuscito').ok('Ok'));
                });
        }

        $scope.choose = function ($event) {
            $log.debug('looking for products');
            $mdDialog.show({
                controller: function($log, $mdDialog, $q, $rootScope, $sce, $scope, $state, APP, ScenariosProductsService) {

                    $scope.languages = $rootScope.languages;

                    var initCategories = function () {
                        return ScenariosCategoriesService.getList({}, 0, 0).then(
                            function (categories) {
                                $scope.categories = categories;
                                return $q.when(true);
                            }
                        );
                    }

                    initCategories();

                    var headers = [
                        {field: 'category.name', label: 'category', sortable: false},
                        {field: 'name', label: 'name', sortable: false},
                        {field: 'preview', label: 'preview', sortable: false},
                    ];
                    var overrides = {
                        pageSize: 5
                    };
                    RsListController($filter, $log, $mdDialog, $q, $scope, $state, APP, ScenariosProductsService, headers, overrides);
                    $scope.pick = function(product) {
                        $mdDialog.hide(product);
                    }
                    $scope.cancel = function () {
                        $mdDialog.hide();
                    }
                },
                templateUrl: 'app/scenarios/dialogs/products-list-dialog.html',
                targetEvent: $event
            }).then(function (product) {
                if (product) {
                    embed(product);
                }
            }, function () {
                $log.debug('no selection');
            })
        };

    });
;
