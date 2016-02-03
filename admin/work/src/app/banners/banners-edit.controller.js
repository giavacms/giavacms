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

    .controller('BannersEditController', function ($filter, $log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, BannersService, BannertypesService, RsResource) {

        var overrides = {
            postSave: function() { $state.go(APP.BASE + 'banners_list'); },
            postUpdate: function() { $state.go(APP.BASE + 'banners_list'); }
        };

        var editFunction = RsEditController($log, $mdDialog, $q, $scope, $state, $stateParams, APP, BannersService, overrides)

        if ( !$scope.element ) {
            $scope.element = {
                online: false,
                internal: true
            };
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

        $scope.baseUrl = APP.PROTOCOL + "://" + APP.HOST + APP.CONTEXT;

        var embed = function(resource) {
            // make the image by the resource parts
            // TODO
            $scope.element.internal = false;
            //$scope.element.url = APP.PROTOCOL + "://" + APP.HOST + APP.CONTEXT + resource.path;
            $scope.element.url = resource.path;
                if ( ! $scope.element.image ) {
                $scope.element.image = {};
            }
            $scope.element.image.filename = $scope.element.url;
        }

        var rootPath = APP.BANNERS.IMAGESPATH;

        $scope.chooseFile = function ($event) {
            $log.debug('looking for graphics');
            $mdDialog.show({
                controller: function($log, $mdDialog, $q, $sce, $scope, APP, RsResource) {
                    var previewType = 'IMAGE';
                    $scope.accepts = "image/*";
                    $scope.pageSize = 5;
                    $scope.from = 0;
                    $scope.to = $scope.pageSize;
                    ResourceController($log, $mdDialog, $q, $sce, $scope, APP, RsResource, rootPath, previewType);
                    $scope.pick = function(resource) {
                        $mdDialog.hide(resource);
                    }
                    $scope.cancel = function () {
                        $mdDialog.hide();
                    }
                },
                templateUrl: 'app/banners/dialogs/images-list-dialog.html',
                targetEvent: $event
            }).then(function (resource) {
                if (resource) {
                    embed(resource);
                }
            }, function () {
                $log.debug('no selection');
            })
        };

        angular.extend(this, new AddFilesController($mdDialog, $sce, $scope, APP, RsResource, rootPath, embed));

    });
;
