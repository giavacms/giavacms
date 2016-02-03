'use strict';

angular.module('giavacms-table')

    .directive('tpager', function () {
        return {
            replace: true,
            restrict: 'A',
            templateUrl: 'components/table/tpager.tmpl.html',

            link: function (scope, element, attrs, ctrl, $transclude) {
                scope.full = attrs.full || true;
            }
        };
    });
