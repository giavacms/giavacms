'use strict';

angular.module('giavacms-table')

    .directive('tsmallpager', function () {
        return {
            replace: true,
            restrict: 'A',
            templateUrl: 'components/table/tsmallpager.tmpl.html',

            link: function (scope, element, attrs, ctrl, $transclude) {
                scope.full = attrs.full || false;
            }
        };
    });
