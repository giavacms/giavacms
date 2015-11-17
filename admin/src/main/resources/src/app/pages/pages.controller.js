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

    })

    .run(function(MenuService, APP) {
        //MenuService.addLink(APP.BASE + "pages", 'List', 2, 'fa fa-list', APP.PAGES.toggle);
        MenuService.addLink(APP.BASE + "pages", 'PAGES', 1400, 'fa fa-html5');
    })

    .controller('PagesController', function($log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, RsResource) {

        var rootPath = '/';

        var previewType = 'STATIC';

        $scope.accepts = "text/html";

        ResourceController($log, $mdDialog, $q, $sce, $scope, APP, RsResource, rootPath, previewType);

    });
;
