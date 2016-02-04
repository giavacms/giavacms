'use strict';

function AddFilesController($mdDialog, $sce, $scope, APP, RsResource, path, callback) {

    var uploadFile = function(fileread) {
        var toUpload = {
            name: fileread.name,
            fileContent: fileread.fileContent
        }
        var reqParams = {};
        reqParams['host'] = APP.HOST;
        reqParams['context'] = APP.CONTEXT;
        reqParams['entityType'] = 'resources';
        reqParams['id'] = path;
        reqParams['entityType2'] = 'files';
        RsResource.create(
            reqParams,
            toUpload,
            function (result) {
                callback(result);
            },
            function (error) {
                $mdDialog.show(
                    $mdDialog.alert().title('Errore').content('Caricamento non riuscito: ' + fileread.name).ok('Ok'));
            }
        );
    }

    $scope.addFile = function ($event) {
        var accepts = $scope.accepts;
        var parentEl = angular.element(document.body);
        $mdDialog.show({
            parent: parentEl,
            targetEvent: $event,
            templateUrl: 'components/resources/newfile.tmpl.html',
            controller: function($scope, $mdDialog) {
                $scope.vm = this;
                $scope.vm.uploadme = {};
                if ( accepts ) {
                    $scope.accepts = accepts;
                }
                $scope.closeDialog = function() {
                     $mdDialog.hide();
                }
                $scope.upload = function() {
                    uploadFile($scope.vm.uploadme);
                    $mdDialog.hide();
                }
            }
        });
    };

    $scope.addFiles = function ($event) {
        var accepts = $scope.accepts;
        var parentEl = angular.element(document.body);
        $mdDialog.show({
            parent: parentEl,
            targetEvent: $event,
            templateUrl: 'components/resources/newfiles.tmpl.html',
            controller: function($scope, $mdDialog) {
                $scope.vm = this;
                $scope.vm.uploadme = [];
                if ( accepts ) {
                    $scope.accepts = accepts;
                }
                $scope.closeDialog = function() {
                     $mdDialog.hide();
                }
                $scope.upload = function() {
                    for( var u = $scope.vm.uploadme.length-1; u >= 0 ; u--) {
                        uploadFile($scope.vm.uploadme[u]);
                        $scope.vm.uploadme.splice(u,1);
                    };
                    $mdDialog.hide();
                }
            }
        });
    };

    $scope.trustSrc = function(src) {
        return $sce.trustAsResourceUrl(src);
    }

}
