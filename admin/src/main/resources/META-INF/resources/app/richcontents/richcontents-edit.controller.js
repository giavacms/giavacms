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

        var confirmAndGoTo = function(toState, skipConfirm) {
            var confirm = null;
            if (skipConfirm) {
                confirm = $q.when(true);
            }
            else {
                confirm = $mdDialog.show(
                    $mdDialog.confirm()
                        .title('Attenzione')
                        .content('Eventuali modifiche non saranno salvate?')
                        .ok('Ok')
                        .cancel('Annulla')
                );
            }
            confirm.then(
                function ok() {
                    $log.debug('You are sure');
                    $state.go(toState, {id: $scope.element.id});
                },
                function cancel() {
                    $log.debug('You are not sure');
                }
            );
        }

        $scope.editDocuments = function(skipConfirm) {
            confirmAndGoTo(APP.BASE + 'richcontents_edit_documents', skipConfirm);
        }

        $scope.editImages = function(skipConfirm) {
            confirmAndGoTo(APP.BASE + 'richcontents_edit_images', skipConfirm);
        }

        var overrides = {
            preSave : function() { return setDates(); },
            preUpdate : function() { return setDates(); },
            postSave: function (ok,element) {
                if (ok) {
                    $scope.element = element;
                    $scope.editDocuments(true);
                }
                else {
                    $mdDialog.show(
                        $mdDialog.alert().title('Errore').content('Salvataggio non riuscito').ok('Ok'));
                }
            },
            postUpdate: function (ok) {
                if (ok) {
                    $scope.editDocuments(true);
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

        $scope.tinymceOptions = {
            resize: true,
            //width: 400,  // I *think* its a number and not '400' string
            height: 500,
            plugins: 'print textcolor table',
            toolbar: "undo redo | fontselect fontsizeselect | bold italic underline | bullist numlist outdent indent | forecolor backcolor | table"
    };

    });
;
