'use strict';

angular.module('giavacms-scenario')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'scenarios_new', {
            // set the url of this page
            url: '/scenarios-new',
            // set the html template to show on this page
            templateUrl: 'app/scenarios/scenarios-edit.html',
            // set the controller to load for this page
            controller: 'ScenariosEditController'
        });

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'scenarios_edit', {
            // set the url of this page
            url: '/scenarios-edit/:id',
            // set the html template to show on this page
            templateUrl: 'app/scenarios/scenarios-edit.html',
            // set the controller to load for this page
            controller: 'ScenariosEditController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "scenarios_new", 'Add Scenario', 60, 'fa fa-plus', APP.CATALOGUE.toggle);
    })

    .controller('ScenariosEditController', function ($filter, $log, $mdDialog, $q, $sce, $scope, $state, $stateParams, APP, ScenariosService, RsResource) {

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
            confirmAndGoTo(APP.BASE + 'scenarios_edit_documents', skipConfirm);
        }

        $scope.editImages = function(skipConfirm) {
            confirmAndGoTo(APP.BASE + 'scenarios_edit_images', skipConfirm);
        }

        $scope.editProducts = function(skipConfirm) {
            confirmAndGoTo(APP.BASE + 'scenarios_edit_products', skipConfirm);
        }

        var overrides = {
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

        var editFunction = RsEditController($log, $mdDialog, $q, $scope, $state, $stateParams, APP, ScenariosService, overrides)

        if ( !$scope.element ) {
            $scope.element = {};
        }

        editFunction.init();

    });
;
