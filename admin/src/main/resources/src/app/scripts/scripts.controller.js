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

        ResourceController($log, $mdDialog, $q, $sce, $scope, APP, RsResource, rootPath, previewType);

    });
;
