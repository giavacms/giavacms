'use strict';

angular.module('giavacms-table')

    .directive('theader', function () {
        return {
            restrict: 'A',
            templateUrl: 'components/table/theader.tmpl.html'
        };
    });
