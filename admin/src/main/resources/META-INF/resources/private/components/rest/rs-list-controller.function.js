'use strict';

function RsListController($filter, $log, $mdDialog, $q, $scope, $state, APP, RsService, headers, overrides) {

    $scope.element = {};

    // overrides
    var defaults = {
        search: {},
        pageSize: 10,
        autoload: true,
        id: 'id',
        shownPages: 5,
        add: function () {
            $state.go(APP.BASE + RsService.entityType + '_new');
        },
        view: function (id) {
            $state.go(APP.BASE + RsService.entityType + '_view', {'id': id});
        },
        edit: function (id) {
            $state.go(APP.BASE + RsService.entityType + '_edit', {'id': id});
        },
        preSave: function () {
            return $q.when(true);
        },
        postSave: function (ok) {
            if (ok) {
                $scope.element = {};
                $scope.refresh();
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
                $scope.refresh();
            }
            else {
                $mdDialog.show(
                    $mdDialog.alert().title('Errore').content('Salvataggio delle modifiche non riuscito').ok('Ok'));
            }
        },
        postBack: function () {
            $state.go(APP.BASE + RsService.entityType + '_list');
        },
        postList: function (data) {
        }
    };

    if (overrides) {
        defaults = angular.extend(defaults, overrides);
    }

    // colonne
    $scope.headers = {field: 'id', label: 'identificativo', sortable: true};
    if (headers) {
        $scope.headers = headers;
    }

    var editables = {};

    $scope.toggle = function (index) {
        if ( angular.isDefined(editables[index]) ) {
            $scope.model.splice(index, 1, editables[index]);
            delete editables[index];
        }
        else {
            editables[index] = angular.copy($scope.model[index]);
        }
    }

    $scope.editable = function (index) {
        return angular.isDefined(editables[index]);
    }

    $scope.resetToggle = function () {
        editables = [];
    }
    // ordinamento locale
    $scope.predicate = defaults.id;
    $scope.reverse = false;
    var toggleSort = function (f) {
        if (f == $scope.predicate) {
            $scope.reverse = !$scope.reverse;
        }
        else {
            $scope.predicate = f;
            $scope.reverse = false;
        }
    }
    // ordinamento locale
    $scope.pageSort = function (f) {
        toggleSort(f);
        $scope.model = $filter('orderBy')($scope.model, $scope.predicate, $scope.reverse);
    };
    // ordinamento remoto
    $scope.sort = function (f) {
        toggleSort(f);
        $scope.startRow = 0;
        $scope.currentPage = 1;
        $scope.refresh();
    };

    // ricerca
    $scope.search = angular.copy(defaults.search);

    // scaricamento dati
    $scope.pageSize = defaults.pageSize;
    $scope.startRow = 0;
    $scope.currentPage = 1;
    $scope.clear = function () {
        $scope.search = angular.copy(defaults.search);
        $scope.refresh();
    }
    $scope.refresh = function () {
        $scope.resetToggle();
        $scope.message = 'Ricerca in corso...';
        var orderBy = $scope.predicate + ($scope.reverse ? ' desc' : ' asc')
        RsService.getList($scope.search, $scope.startRow, $scope.pageSize, orderBy).then(
            // successo
            function (data) {
                defaults.postList(data);
                if (data) {
                    $scope.model = data;
                    // nessun risultato
                    if ($scope.model.length == 0) {
                        $scope.message = 'Nessun risultato.';
                        $scope.listSize = 0;
                        $scope.pages = [];
                        // solo un sottoinsieme delle pagine verrà mostrato come link diretto nella pagina
                        $scope.subpages = [];
                    }
                    // ci sono dati. calcolo le pagine
                    else {
                        $scope.listSize = RsService.getSize();
                        $scope.message = 'Totale: ' + $scope.listSize + ' risultat' + ($scope.listSize == 1 ? 'o' : 'i');
                        $scope.pages = [];
                        var p = 0;
                        for (var i = 1; i <= Number($scope.listSize); i += Number($scope.pageSize)) {
                            p++;
                            $scope.pages.push(p);
                        }

                        // solo un sottoinsieme delle pagine verrà mostrato come link diretto nella pagina
                        $scope.subpages = [];
                        var halfPages = Math.round(defaults.shownPages / 2);
                        for (var s = 1; s <= defaults.shownPages; s++) {
                            var subpage = $scope.currentPage - halfPages + s;
                            if ($scope.pages.indexOf(subpage) >= 0) {
                                $scope.subpages.push(subpage);
                            }
                        }
                        var missing = defaults.shownPages - $scope.subpages.length;
                        if (missing > 0) {
                            if ($scope.subpages[0] == 1) {
                                for (var m = 0; m < missing; m++) {
                                    var lastPage = $scope.subpages[$scope.subpages.length - 1];
                                    if ($scope.pages.indexOf(lastPage + 1) >= 0) {
                                        $scope.subpages.push(lastPage + 1);
                                    }
                                }
                            }
                            else {
                                for (var m = 0; m < missing; m++) {
                                    var firstPage = $scope.subpages[0];
                                    if (firstPage - 1 > 0) {
                                        $scope.subpages.unshift(firstPage - 1);
                                    }
                                }
                            }
                        }
                    }
                }
            },
            // errorre
            function () {
                if (!$scope.model) {
                    $scope.message = 'Dati non disponibili.';
                }
                $mdDialog.show(
                    $mdDialog.alert().title('Errore').content('Dati non disponibili').ok('Ok')
                );
            })
    }
    if (defaults.autoload) {
        $scope.refresh();
    }
    else {
        $scope.message = 'Esegui una ricerca';
    }

    // paginazione
    $scope.firstPage = function () {
        $scope.goToPage(1);
    }
    $scope.goToPage = function (pageNo) {
        $scope.startRow = (pageNo - 1) * $scope.pageSize;
        $scope.refresh();
        $scope.currentPage = pageNo;
    }
    $scope.lastPage = function () {
        $scope.goToPage($scope.pages.length);
    }
    $scope.rowsPerPage = function (pageSize) {
        $scope.pageSize = pageSize;
        $scope.startRow = 0;
        $scope.refresh();
        $scope.currentPage = 1;
    }

    $scope.add = function () {
        defaults.add();
    }
    $scope.view = function (id) {
        defaults.view(id);
    }
    $scope.edit = function (id) {
        defaults.edit(id);
    }
    $scope.back = function () {
        defaults.postBack();
    }


    // salvataggio
    $scope.updateInLine = function (element, skipConfirm) {
        var confirm = null;
        if (skipConfirm) {
            confirm = $q.when(true);
        }
        else {
            confirm = $mdDialog.show(
                $mdDialog.confirm()
                    .title('Conferma')
                    .content('Confermi le modifiche?')
                    .ok('Ok')
                    .cancel('Annulla')
            );
        }
        confirm.then(
            function ok() {
                $log.debug('You are sure');
                return defaults.preUpdate().then(function (ok) {
                    if (ok) {
                        return RsService.update(element, element[defaults.id]).then(
                            function succes(data) {
                                return defaults.postUpdate(data);
                            },
                            function fail() {
                                $mdDialog.show(
                                    $mdDialog.alert().title('Errore').content('Aggiornamento non riuscito').ok('Ok'));
                            });
                    } else {
                        return;
                    }
                });

            },
            function cancel() {
                $log.debug('You are not sure');
                $scope.resetToggle();
                return;
            }
        );
    }

    // eliminazione
    $scope.deleteInLine = function (element, skipConfirm) {
        var confirm = null;
        if (skipConfirm) {
            confirm = $q.when(true);
        }
        else {
            confirm = $mdDialog.show(
                $mdDialog.confirm()
                    .title('Conferma')
                    .content('Confermi l\'eliminazione ?')
                    .ok('Ok')
                    .cancel('Annulla')
            );
        }
        confirm.then(
            function ok() {
                $log.debug('You are sure');
                return RsService.delete(element, element[defaults.id]).then(function () {
                    $scope.refresh();
                    return;
                });
            },
            function cancel() {
                $log.debug('You are not sure');
                return;
            }
        );
    }

    // salvataggio
    $scope.save = function (skipConfirm) {
        var confirm = null;
        if (skipConfirm) {
            confirm = $q.when(true);
        }
        else {
            confirm = $mdDialog.show(
                $mdDialog.confirm()
                    .title('Conferma')
                    .content('Confermi i dati inseriti?')
                    .ok('Ok')
                    .cancel('Annulla')
            );
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
                return;
            }
        );
    }

}
