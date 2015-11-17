'use strict';

angular.module('giavacms-documents')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        $stateProvider.state( APP.BASE + 'documents', {
            // set the url of this page
            url: '/documents',
            // set the html template to show on this page
            templateUrl: 'app/documents/documents-list.html',
            // set the controller to load for this page
            controller: 'DocumentsController'
        });

    })

    .run(function(MenuService, APP) {
        // MenuService.addLink(APP.BASE + "documents", 'List', 1, 'fa fa-file', APP.DOCUMENTS.toggle);
        MenuService.addLink(APP.BASE + "documents", 'DOCUMENTS', 1500, 'fa fa-file');
    })

    .controller('DocumentsController', function($log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, RsResource) {

        var rootPath = '/';

        $scope.accepts = "*";

        ResourceController($log, $mdDialog, $q, $sce, $scope, APP, RsResource, rootPath);

    });
;
