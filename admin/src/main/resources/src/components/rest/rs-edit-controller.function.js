'use strict';

function RsEditController($log, $mdDialog, $q, $scope, $state, $stateParams, RsService, overrides) {

    // overrides
    var defaults = {
        id: 'id',
        preSave: function () {
            return $q.when(true);
        },
        postSave: function (ok) {
            if (ok) {
                $state.go('admin.default.' + RsService.entityType + '_list');
            }
            else {
                $mdDialog.show(
                    $mdDialog.alert().title('Errore').content('Salvataggio non riuscito').ok('Ok'));
            }
        },
        preUpdate: function () {
            return $q.when(true);
        },
        postUpdate: function (ok) {
            if (ok) {
                $state.go('admin.default.' + RsService.entityType + '_view', {id: $scope.element.id});
            }
            else {
                $mdDialog.show(
                    $mdDialog.alert().title('Errore').content('Salvataggio delle modifiche non riuscito').ok('Ok'));
            }
        },
        postFetch: function() {
        },
        postBack: function () {
            $state.go('admin.default.' + RsService.entityType + '_list');
        }
    };
    if (overrides) {
        defaults = angular.extend(defaults, overrides);
    }

    $scope.loaded = false;

    // caricamento
    if ($stateParams.id && !isNaN($stateParams.id)) {
        $scope.element = {id: $stateParams.id};
        RsService.getElement($stateParams.id).then(function (element) {
            $scope.element = element;
            defaults.postFetch();
            $scope.loaded = true;
        }, function () {
            $mdDialog.show(
                $mdDialog.alert().title('Errore').content('Dati non disponibili').ok('Ok')
            );
        });
    }

    // salvataggio
    $scope.save = function (skipConfirm) {
        var confirm =
            $mdDialog.show(
                $mdDialog.confirm()
                    .title('Conferma')
                    .content('Confermi i dati inseriti?')
                    .ok('Ok')
                    .cancel('Annulla')
            );
        if (skipConfirm) {
            confirm = $q.when(true);
        }
        confirm.then(
            function ok() {
                $log.debug('You are sure');
                return defaults.preSave().then(
                    function canSave(ok) {
                        if (ok) {
                            return RsService.save($scope.element).then(
                                function () {
                                    return defaults.postSave(true);
                                },
                                function () {
                                    return defaults.postSave(false);
                                }
                            );
                        }
                        else {
                            return;
                        }
                    }
                );
            },
            function cancel() {
                $log.debug('You are not sure');
            }
        );
    }

    // salvataggio
    $scope.update = function (skipConfirm) {
        var confirm = $mdDialog.show(
            $mdDialog.confirm()
                .title('Conferma')
                .content('Confermi le modifiche?')
                .ok('Ok')
                .cancel('Annulla')
        );
        if (skipConfirm) {
            confirm = $q.when(true);
        }
        confirm.then(
            function ok() {
                $log.debug('You are sure');
                return defaults.preUpdate().then(
                    function canUpdate(ok) {
                        if (ok) {
                            return RsService.update($scope.element, $scope.element[defaults.id]).then(
                                function () {
                                    return defaults.postUpdate(true);
                                },
                                function () {
                                    return defaults.postUpdate(false);
                                }
                            );
                        }
                        else {
                            return;
                        }
                    }
                );
            },
            function cancel() {
                $log.debug('You are not sure');
            }
        );
    }

    // eliminazione
    $scope.delete = function (skipConfirm) {
        var confirm = $mdDialog.show(
            $mdDialog.confirm()
                .title('Conferma')
                .content('Confermi l\'eliminazione ?')
                .ok('Ok')
                .cancel('Annulla')
        );
        if (skipConfirm) {
            confirm = $q.when(true);
        }
        confirm.then(
            function ok() {
                $log.debug('You are sure');
                return RsService.delete($scope.element, $scope.element[defaults.id]).then(
                    function () {
                        return defaults.postBack();
                    },
                    function () {
                        $mdDialog.show(
                            $mdDialog.alert().title('Errore').content('Eliminazione non riuscita').ok('Ok'));
                        return false;
                    }
                );
            },
            function cancel() {
                $log.debug('You are not sure');
            }
        );
    }

    // annullamento
    $scope.back = function (skipConfirm) {
        var confirm =
            $mdDialog.show(
                $mdDialog.confirm()
                    .title('Conferma')
                    .content('Esci senza salvare?')
                    .ok('Ok')
                    .cancel('Annulla')
            );
        if (skipConfirm) {
            confirm = $q.when(true);
        }
        confirm.then(
            function ok() {
                $log.debug('You are sure');
                return defaults.postBack();
            },
            function cancel() {
                $log.debug('You are not sure');
                return false;
            }
        );
    }

}
