'use strict';

angular.module('giavacms-styles')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        $stateProvider.state( APP.BASE + 'styles', {
            // set the url of this page
            url: '/styles',
            // set the html template to show on this page
            templateUrl: 'app/styles/styles-list.html',
            // set the controller to load for this page
            controller: 'StylesController'
        });

    })

    .run(function(MenuService, APP) {
        //MenuService.addLink(APP.BASE + "styles", 'List', 2, 'fa fa-list', APP.STYLES.toggle);
        MenuService.addLinkWithACL('STYLES', APP.BASE + "styles", 'STYLES', 1300, 'fa fa-css3');
    })

    .controller('StylesController', function($log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, RsResource) {

        var rootPath = APP.STYLES.PATH;

        var previewType = 'STYLESHEET';

        $scope.accepts = "text/css";

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
            mode: 'css',
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
