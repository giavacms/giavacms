'use strict';

angular.module('giavacms-table')

    .directive('tfooter', function () {
        return {
            replace: true,
            restrict: 'A',
            templateUrl: 'components/table/tfooter.tmpl.html'
        };
    });
