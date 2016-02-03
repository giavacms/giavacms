'use strict';

angular.module('giavacms-catalogue')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'products_edit_images', {
            // set the url of this page
            url: '/products-edit/:id/images',
            // set the html template to show on this page
            templateUrl: 'app/products/products-edit-images.html',
            // set the controller to load for this page
            controller: 'ProductsEditImagesController'
        });

    })

    .controller('ProductsEditImagesController', function ($filter, $http, $log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, ProductsService, RsResource) {

        var preview = function (document) {
            document.preview = APP.PROTOCOL + "://" + APP.HOST + APP.CONTEXT + document.filename;
            return $q.when(true);
        }

        var initElement = function () {
            return ProductsService.getElement($stateParams.id).then(
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

        var initResources = function() {
            var reqParams = {};
            reqParams['host'] = APP.HOST;
            reqParams['context'] = APP.CONTEXT;
            reqParams['entityType'] = 'products';
            reqParams['id'] = $stateParams.id;
            reqParams['entityType2'] = 'images';
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
            {field: 'name', label: 'name', sortable: false},
            {field: 'type', label: 'type', sortable: false},
            {field: 'preview', label: 'preview', sortable: false},
        ];

        initElement().then(
            function() {
                initResources();
            }
        );

        $scope.delete = function (resource, skipConfirm) {
            var confirm = null;
            if (skipConfirm) {
                confirm = $q.when(true);
            }
            else {
                confirm = $mdDialog.show(
                    $mdDialog.confirm()
                        .title('Conferma')
                        .content('Confermi l\'eliminazione di ' + resource.name + ' ?')
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
                    reqParams['entityType'] = 'products';
                    reqParams['id'] = $stateParams.id;
                    reqParams['entityType2'] = 'images';
                    reqParams['id2'] = resource.id;
                    return RsResource.delete(reqParams,
                        function () {
                            return initResources();
                        },
                        function (what) {
                            return initResources();
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
            $state.go(APP.BASE + 'products_edit_documents', {id: $stateParams.id});
        }

        $scope.editImages = function() {
            $state.go(APP.BASE + 'products_edit_images', {id: $stateParams.id});
        }

        $scope.editElement = function() {
            $state.go(APP.BASE + 'products_edit', {id: $stateParams.id});
        }

        var rootPath = APP.RICHCONTENTS.IMAGESPATH;

        var embed = function(resource) {
            var uploadUrl = APP.PROTOCOL + "://" + APP.HOST + APP.CONTEXT + '/api/v1/products/' + $stateParams.id + '/images/' + resource.path;
            $http.post(uploadUrl)
                .success(function(){
                    initResources();
                })
                .error(function(){
                    $mdDialog.show(
                        $mdDialog.alert().title('Errore').content('Salvataggio non riuscito').ok('Ok'));
                });
        }

        new AddFilesController($mdDialog, $sce, $scope, APP, RsResource, rootPath, embed);

        $scope.chooseFile = function ($event) {
            $log.debug('looking for documents');
            $mdDialog.show({
                controller: function($log, $mdDialog, $q, $sce, $scope, APP, RsResource) {
                    var previewType = 'IMAGE';
                    $scope.accepts = 'image/*';
                    $scope.pageSize = 5;
                    $scope.from = 0;
                    $scope.to = $scope.pageSize;
                    ResourceController($log, $mdDialog, $q, $sce, $scope, APP, RsResource, rootPath, previewType);
                    $scope.pick = function(resource) {
                        $mdDialog.hide(resource);
                    }
                    $scope.cancel = function () {
                        $mdDialog.hide();
                    }
                },
                templateUrl: 'app/products/dialogs/images-list-dialog.html',
                targetEvent: $event
            }).then(function (resource) {
                if (resource) {
                    embed(resource);
                }
            }, function () {
                $log.debug('no selection');
            })
        };

    });
;
