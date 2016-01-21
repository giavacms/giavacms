'use strict';

angular.module('giavacms-images')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        $stateProvider.state( APP.BASE + 'images', {
            // set the url of this page
            url: '/images',
            // set the html template to show on this page
            templateUrl: 'app/images/images-list.html',
            // set the controller to load for this page
            controller: 'ImagesController'
        });

    })

    .run(function(MenuService, APP) {
        // MenuService.addLink(APP.BASE + "images", 'List', 1, 'fa fa-list', APP.IMAGES.toggle);
        MenuService.addLinkWithACL('IMAGES', APP.BASE + "images", 'IMAGES', 1100, 'fa fa-picture-o');
    })

    .controller('ImagesController', function($log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, RsResource) {

        var rootPath = APP.IMAGES.PATH;

        var previewType = 'IMAGE';

        $scope.accepts = "image/*";

        $scope.from = 0;
        $scope.to = 10;

        ResourceController($log, $mdDialog, $q, $sce, $scope, APP, RsResource, rootPath, previewType);

    });
;
