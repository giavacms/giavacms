angular.module('giavacms-misc')

    .service('Utils', function () {

        var getDate = function () {
            var data = null;
            var currentTime = new Date()
            data = currentTime.getDate() + "/" + currentTime.getMonth() + 1 + "/" + currentTime.getFullYear();
            return data;
        };

        var getMaxId = function (lista) {
            var max = 0;
            for (var i = 0; i < lista.length; i++) {
                if (lista[i].id > max) {
                    max = lista[i].id;
                }
            }
            return ++max;
        }

        var toItems = function (lista, label) {
            var items = [];
            if (label) {
                items.push({label: label, value: undefined});
            }
            if (lista) {
                lista.forEach(function (item) {
                    items.push({label: item, value: item});
                });
            }
            return items;
        }

        return {
            getMaxId: getMaxId,
            getDate: getDate,
            toItems: toItems
        };

    })

;
