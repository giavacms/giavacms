'use strict';

angular.module('giavacms-scenario')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'scenarios_edit_documents', {
            // set the url of this page
            url: '/scenarios-edit/:id/documents',
            // set the html template to show on this page
            templateUrl: 'app/scenarios/scenarios-edit-documents.html',
            // set the controller to load for this page
            controller: 'ScenariosEditDocumentsController'
        });

    })

    .controller('ScenariosEditDocumentsController', function ($filter, $http, $log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, ScenariosService, RsResource) {

        var preview = function (document) {
            document.preview = APP.PROTOCOL + "://" + APP.HOST + APP.CONTEXT + document.filename;
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

        var initResources = function() {
            var reqParams = {};
            reqParams['host'] = APP.HOST;
            reqParams['context'] = APP.CONTEXT;
            reqParams['entityType'] = 'scenarios';
            reqParams['id'] = $stateParams.id;
            reqParams['entityType2'] = 'documents';
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
                    reqParams['entityType'] = 'scenarios';
                    reqParams['id'] = $stateParams.id;
                    reqParams['entityType2'] = 'documents';
                    reqParams['id2'] = resource.id;
                    return RsResource.delete(reqParams,
                        function () {
                            return initResources();
                        },
                        function () {
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

        var rootPath = APP.SCENARIO.DOCUMENTSPATH;

        var embed = function(resource) {
            var uploadUrl = APP.PROTOCOL + "://" + APP.HOST + APP.CONTEXT + '/api/v1/scenarios/' + $stateParams.id + '/documents/' + resource.path;
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
                    //var previewType = 'IMAGE';
                    $scope.accepts = "*"; //image/*";
                    $scope.pageSize = 5;
                    $scope.from = 0;
                    $scope.to = $scope.pageSize;
                    ResourceController($log, $mdDialog, $q, $sce, $scope, APP, RsResource, rootPath); //, previewType);
                    $scope.pick = function(resource) {
                        $mdDialog.hide(resource);
                    }
                    $scope.cancel = function () {
                        $mdDialog.hide();
                    }
                },
                templateUrl: 'app/scenarios/dialogs/documents-list-dialog.html',
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
