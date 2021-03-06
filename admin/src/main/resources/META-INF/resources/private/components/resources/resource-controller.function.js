'use strict';

function ResourceController($log, $mdDialog, $q, $sce, $scope, APP, RsResource, rootPath, previewType) {

    $scope.headers = [
                {field: 'name', label: 'name', sortable: false},
                {field: 'type', label: 'type', sortable: false},
                {field: 'preview', label: 'preview', sortable: false},
            ];

    $scope.search = {};

    //$scope.root = '/static/' + rootPath;
    $scope.root = rootPath;
    $scope.type = previewType;

    $scope.folder = {
        path: $scope.root,
        type: 'folder',
        name: rootPath
    };

    $scope.open = function(resource) {
        if ( resource.type == 'folder' ) {
            $scope.loadResources(resource);
        }
        else {
            $scope.edit(resource);
        }
    }

    var preview = function(r) {
        if ( r.type == 'folder' ) {
            var reqParams = {};
            reqParams['host'] = APP.HOST;
            reqParams['context'] = APP.CONTEXT;
            reqParams['entityType'] = 'resources';
            reqParams['id'] = r.path;
            RsResource.query(reqParams, function (data) {
                if ( data && data.length > 0 ) {
                    r.empty = false;
                }
                else {
                    r.empty = true;
                }
            });
        }
        else if ( $scope.type ) {
            if ( r.type == $scope.type ) {
                r.preview = APP.PROTOCOL + "://" + APP.HOST + APP.CONTEXT + r.path;
            }
        }
        else {
            r.preview = APP.PROTOCOL + "://" + APP.HOST + APP.CONTEXT + r.path;
        }
    };

    $scope.loadResources = function (folder) {
        if ( folder.path != $scope.folder.path ) {
            $scope.previous = $scope.folder;
        }
        $scope.folder = folder;
        $scope.ok = false;
        var reqParams = {};
        reqParams['host'] = APP.HOST;
        reqParams['context'] = APP.CONTEXT;
        reqParams['entityType'] = 'resources';
        if (folder !== '') {
            reqParams['id'] = folder.path;
        }
        RsResource.query(reqParams,
            function (data) {
                $scope.resources = data;
                data.forEach(
                    function(r)  {
                        preview(r);
                    }
                );
            }
        );
        new AddFilesController($mdDialog, $sce, $scope, APP, RsResource, $scope.folder.path, afterAdd);
        return $q.when(true);
    };

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
                $scope.ok = false;
                var reqParams = {};
                reqParams['host'] = APP.HOST;
                reqParams['context'] = APP.CONTEXT;
                reqParams['entityType'] = 'resources';
                reqParams['id'] = resource.folder;
                reqParams['entityType2'] = 'files';
                reqParams['id2'] = resource.name;
                return RsResource.delete(reqParams,
                    function () {
                        return $scope.loadResources($scope.folder);
                    }
                );
            },
            function cancel() {
                $log.debug('You are not sure');
                return $q.when(false);
            }
        );
    }

    var createAndGoto = function(subfolder)
    {
        var reqParams = {};
        reqParams['host'] = APP.HOST;
        reqParams['context'] = APP.CONTEXT;
        reqParams['entityType'] = 'resources';
        reqParams['id'] = $scope.folder.path;
        RsResource.create(reqParams, subfolder, function (result) {
            $scope.loadResources(result);
        });
    }

    $scope.addFolder = function ($event) {
        var parentEl = angular.element(document.body);
        $mdDialog.show({
            parent: parentEl,
            targetEvent: $event,
            templateUrl: 'components/resources/newfolder.tmpl.html',
            locals: {
                subfolder: {}
            },
            controller: function($scope, $mdDialog) {
                $scope.closeDialog = function() {
                     $mdDialog.hide();
                }
                $scope.mkdir = function() {
                    createAndGoto($scope.subfolder);
                    $mdDialog.hide();
                }
            }
        });
    };

    $scope.ok = false;
    $scope.loadResources($scope.folder);

    $scope.edit = function(resource) {
        var reqParams = {};
        reqParams['host'] = APP.HOST;
        reqParams['context'] = APP.CONTEXT;
        reqParams['entityType'] = 'resources';
        reqParams['id'] = resource.folder; // TO CHECK
        reqParams['entityType2'] = 'files';
        reqParams['id2'] = resource.name;
        RsResource.show(reqParams,
            function (data) {
                $scope.resource = data;
            }
        );
    }

    $scope.remove = function() {
        $scope.delete($scope.resource).then(
            function(result) {
                if ( result ) {
                    delete $scope.resource;
                    return $q.when(result);
                }
            }
        );
    }

    $scope.cancel = function() {
        delete $scope.resource;
    }

    $scope.update = function() {
        var reqParams = {};
        reqParams['host'] = APP.HOST;
        reqParams['context'] = APP.CONTEXT;
        reqParams['entityType'] = 'resources';
        reqParams['id'] = $scope.resource.folder; // TO CHECK
        reqParams['entityType2'] = 'files';
        reqParams['id2'] = $scope.resource.name;
        RsResource.update(reqParams, $scope.resource,
            function (data) {
                delete $scope.resource;
                $scope.loadResources($scope.folder);
            },
            function(error) {
                // TODO
            }
        );
    }

    var afterAdd = function() {
        $scope.loadResources($scope.folder);
    }

    new AddFilesController($mdDialog, $sce, $scope, APP, RsResource, $scope.folder.path, afterAdd);

}
