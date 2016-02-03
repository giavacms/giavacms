'use strict';

angular.module('giavacms-scripts')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        $stateProvider.state( APP.BASE + 'scripts', {
            // set the url of this page
            url: '/scripts',
            // set the html template to show on this page
            templateUrl: 'app/scripts/scripts-list.html',
            // set the controller to load for this page
            controller: 'ScriptsController'
        });

    })

    .run(function(MenuService, APP) {
        //MenuService.addLink(APP.BASE + "scripts", 'List', 1, 'fa fa-list', APP.SCRIPTS.toggle);
        MenuService.addLinkWithACL('SCRIPTS', APP.BASE + "scripts", 'SCRIPTS', 1200, 'fa fa-cogs');
    })

    .controller('ScriptsController', function($log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, RsResource) {

        var rootPath = APP.SCRIPTS.PATH;

        var previewType = 'JAVASCRIPT';

        $scope.accepts = "application/javascript";

        $scope.pageSize= 10;
        $scope.from = 0;
        $scope.to = $scope.pageSize;

        ResourceController($log, $mdDialog, $q, $sce, $scope, APP, RsResource, rootPath, previewType);

        // http://stackoverflow.com/questions/28241198/how-to-get-the-value-from-ace-angular-editor
        $scope.aceLoaded = function(_editor) {
            $scope.aceSession = _editor.getSession();
            $scope.aceSession.getDocument().setValue($scope.resource.fileContent);
        };
        $scope.aceChanged = function () {
            $scope.resource.fileContent = $scope.aceSession.getDocument().getValue();
        };

        $scope.aceOptions = {
            onChange: $scope.aceChanged,
            mode: 'javascript',
            onLoad: function (editor) {
                $scope.aceLoaded(editor);
                // defaults
                editor.setTheme("ace/theme/github");
                // options
                editor.setOptions({
                    showGutter: true,
                    showPrintMargin: false,
                });
            }
        };

        if ( $stateParams.id ) {
            var reqParams = {};
            reqParams['host'] = APP.HOST;
            reqParams['context'] = APP.CONTEXT;
            reqParams['entityType'] = 'resources';
            reqParams['id'] = rootPath;
            reqParams['entityType2'] = 'files';
            reqParams['id2'] = $stateParams.id;
            RsResource.show(reqParams,
                function (data) {
                    $scope.resource = data;
                }
            );
        }

    });
;
