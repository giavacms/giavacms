'use strict';

angular.module('giavacms-richcontents')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'richcontents_new', {
            // set the url of this page
            url: '/richcontents-new',
            // set the html template to show on this page
            templateUrl: 'app/richcontents/richcontents-edit.html',
            // set the controller to load for this page
            controller: 'RichcontentsEditController'
        });

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
        $stateProvider.state( APP.BASE + 'richcontents_edit_documents', {
            // set the url of this page
            url: '/richcontents-edit/:id/documents',
            // set the html template to show on this page
            templateUrl: 'app/richcontents/richcontents-edit-documents.html',
            // set the controller to load for this page
            controller: 'RichcontentsEditController'
        });

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'richcontents_edit_graphics', {
            // set the url of this page
            url: '/richcontents-edit/:id/graphics',
            // set the html template to show on this page
            templateUrl: 'app/richcontents/richcontents-edit-graphics.html',
            // set the controller to load for this page
            controller: 'RichcontentsEditController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "richcontents_new", 'Add RichContent', 40, 'fa fa-plus', APP.RICHCONTENTS.toggle);
    })

    .controller('RichcontentsEditController', function ($filter, $log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, RichcontentsService, RichcontenttypesService, RsResource) {

        var setDates = function() {
            if ( $scope.element && $scope.element.dateFmt ) {
                $scope.element.date = $scope.element.dateFmt.getTime();
                delete $scope.element.dateFmt;
            }
            return $q.when(true);
        }
        var overrides = {
            preSave : function() { return setDates(); },
            preUpdate : function() { return setDates(); },
            postSave: function (ok) {
                if (ok) {
                    $state.go(APP.BASE + 'richcontents_edit_documents', {id: $scope.element.id});
                }
                else {
                    $mdDialog.show(
                        $mdDialog.alert().title('Errore').content('Salvataggio non riuscito').ok('Ok'));
                }
            },
            postUpdate: function (ok) {
                if (ok) {
                    $state.go(APP.BASE + 'richcontents_edit_documents', {id: $scope.element.id});
                }
                else {
                    $mdDialog.show(
                        $mdDialog.alert().title('Errore').content('Salvataggio delle modifiche non riuscito').ok('Ok'));
                }
            }

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

        var initDates = function() {
            if ( $scope.element && $scope.element.date ) {
                $scope.element.dateFmt = new Date($scope.element.date);
            }
        }

        editFunction.init().then(
            function(result) {
                if ( result ) {
                    initRichContentTypes();
                    initDates();
                 }
            }
        );

    });
;
