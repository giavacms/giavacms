'use strict';

angular.module('giavacms-richcontents')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'richcontents_edit', {
            // set the url of this page
            url: '/richcontents-edit/:id',
            // set the html template to show on this page
            templateUrl: 'app/richcontents/richcontents-edit.html',
            // set the controller to load for this page
            controller: 'RichcontentsEditController'
        });

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'richcontents_new', {
            // set the url of this page
            url: '/richcontents-new',
            // set the html template to show on this page
            templateUrl: 'app/richcontents/richcontents-edit.html',
            // set the controller to load for this page
            controller: 'RichcontentsEditController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "richcontents_new", 'Add RichContent', 40, 'fa fa-plus', APP.RICHCONTENTS.toggle);
    })

    .controller('RichcontentsEditController', function ($filter, $log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, RichcontentsService, RichcontenttypesService, RsResource) {

        var overrides = {
        };

        var editFunction = RsEditController($log, $mdDialog, $q, $scope, $state, $stateParams, APP, RichcontentsService, overrides)

        if ( !$scope.element ) {
            $scope.element = {};
        }

        if ( !$scope.element.richContentType ) {
            // ng-select riempie tutto l'oggetto. se lo inizializzo non funziona il controllo required
            //$scope.element.bannerType = {};
        }

        $scope.richContentTypes = [];

        var initRichContentTypes = function () {
            return RichcontenttypesService.getList({}, 0, 0).then(
                function (richContentTypes) {
                    $scope.richContentTypes = richContentTypes;
                    richContentTypes.forEach(function (richContentType) {
                            if ($scope.element.richContentType && $scope.element.richContentType.id == richContentType.id) {
                                $scope.element.richContentType = richContentType;
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
                    initRichContentTypes();
                 }
            }
        );

    });
;
