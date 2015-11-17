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
        MenuService.addLink(APP.BASE + "styles", 'STYLES', 1300, 'fa fa-css3');
    })

    .controller('StylesController', function($log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, RsResource) {

        var rootPath = '/static/styles';

        var previewType = 'STYLESHEET';

        $scope.accepts = "text/css";

        ResourceController($log, $mdDialog, $q, $sce, $scope, APP, RsResource, rootPath, previewType);

    });
;
