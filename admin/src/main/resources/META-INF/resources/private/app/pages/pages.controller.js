'use strict';

angular.module('giavacms-pages')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        $stateProvider.state( APP.BASE + 'pages', {
            // set the url of this page
            url: '/pages',
            // set the html template to show on this page
            templateUrl: 'app/pages/pages-list.html',
            // set the controller to load for this page
            controller: 'PagesController'
        });

        $stateProvider.state( APP.BASE + 'pages-edit', {
            // set the url of this page
            url: '/pages/:id',
            // set the html template to show on this page
            templateUrl: 'app/pages/pages-list.html',
            // set the controller to load for this page
            controller: 'PagesController'
        });

    })

    .run(function(MenuService, APP) {
        //MenuService.addLink(APP.BASE + "pages", 'List', 2, 'fa fa-list', APP.PAGES.toggle);
        MenuService.addLinkWithACL('PAGES', APP.BASE + "pages", 'PAGES', 1500, 'fa fa-html5');
    })

    .controller('PagesController', function($log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, RsResource) {

        var rootPath = APP.PAGES.PATH;

        var previewType = 'STATIC';

        $scope.accepts = "text/html";

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
            mode: 'html',
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

