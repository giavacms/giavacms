'use strict';

angular.module('giavacms-graphics')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        $stateProvider.state( APP.BASE + 'graphics', {
            // set the url of this page
            url: '/graphics',
            // set the html template to show on this page
            templateUrl: 'app/graphics/graphics-list.html',
            // set the controller to load for this page
            controller: 'GraphicsController'
        });

    })

    .run(function(MenuService, APP) {
        // MenuService.addLink(APP.BASE + "graphics", 'List', 1, 'fa fa-list', APP.GRAPHICS.toggle);
        MenuService.addLink(APP.BASE + "graphics", 'GRAPHICS', 1100, 'fa fa-picture-o');
    })

    .controller('GraphicsController', function($log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, RsResource) {

        var rootPath = '/static/graphics';

        var previewType = 'IMAGE';

        $scope.accepts = "image/*";

        ResourceController($log, $mdDialog, $q, $sce, $scope, APP, RsResource, rootPath, previewType);

    });
;
