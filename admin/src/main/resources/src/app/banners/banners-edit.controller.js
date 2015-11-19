'use strict';

angular.module('giavacms-banners')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'banners_edit', {
            // set the url of this page
            url: '/banners-edit/:id',
            // set the html template to show on this page
            templateUrl: 'app/banners/banners-edit.html',
            // set the controller to load for this page
            controller: 'BannersEditController'
        });

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'banners_new', {
            // set the url of this page
            url: '/banners-new',
            // set the html template to show on this page
            templateUrl: 'app/banners/banners-edit.html',
            // set the controller to load for this page
            controller: 'BannersEditController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "banners_new", 'Add Banner', 40, 'fa fa-plus', APP.BANNERS.toggle);
    })

    .controller('BannersEditController', function ($filter, $log, $mdDialog, $q, $scope, $state, $stateParams, APP, BannersService, BannertypesService) {

        var overrides = {
        };

        var editFunction = RsEditController($log, $mdDialog, $q, $scope, $state, $stateParams, APP, BannersService, overrides)

        if ( !$scope.element ) {
            $scope.element = {};
        }

        if ( !$scope.element.bannerType ) {
            // ng-select riempie tutto l'oggetto. se lo inizializzo non funziona il controllo required
            //$scope.element.bannerType = {};
        }

        $scope.bannerTypes = [];

        var initBannerTypes = function () {
            return BannertypesService.getList({}, 0, 0).then(
                function (bannerTypes) {
                    $scope.bannerTypes = bannerTypes;
                    bannerTypes.forEach(function (bannerType) {
                            if ($scope.element.bannerType && $scope.element.bannerType.id == bannerType.id) {
                                $scope.element.bannerType = bannerType;
                            }
                        }
                    );
                    return $q.when(true);
                }
            );
        }

        editFunction.init().then(
            function(result) {
                if ( result ) {
                    initBannerTypes();
                 }
            }
        );

        //lista dei materiali
        $scope.openGraphics = function ($event) {
            $log.debug('looking for graphics');
            $mdDialog.show({
                controller: function($log, $mdDialog, $q, $sce, $scope, APP, RsResource) {
                    var rootPath = '/static/graphics';
                    var previewType = 'IMAGE';
                    $scope.accepts = "image/*";
                    ResourceController($log, $mdDialog, $q, $sce, $scope, APP, RsResource, rootPath, previewType);
                    $scope.pick = function(resource) {
                        $mdDialog.hide(resource);
                    }
                    $scope.cancel = function () {
                        $mdDialog.hide();
                    }
                },
                templateUrl: 'app/banners/dialogs/graphics-list-dialog.html',
                targetEvent: $event
            }).then(function (resource) {
                if (resource) {
                    // make the image by the resource parts
                    // TODO
                    $scope.element.internal = false;
                    $scope.element.url = resource.preview;
                }
            }, function () {
                $log.debug('no selection');
            })
        }

    });
;
