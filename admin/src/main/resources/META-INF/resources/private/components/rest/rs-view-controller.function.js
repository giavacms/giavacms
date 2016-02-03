'use strict';

function RsViewController($log, $mdDialog, $scope, $state, $stateParams, RsService, overrides) {


    // overrides
    var defaults = {
        postFetch: function () {
        },
        edit: function (id) {
            $state.go('admin.default.' + RsService.entityType + '_edit', {'id': id});
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
            $scope.loaded = true;
            defaults.postFetch();
        }, function () {
            $mdDialog.show(
                $mdDialog.alert().title('Errore').content('Dati non disponibili').ok('Ok')
            );
        });
    }

    // vai in modifica
    $scope.edit = function (id) {
        defaults.edit(id);
    }
}
