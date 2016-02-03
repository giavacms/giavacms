'use strict';

angular.module('giavacms-menu')

    .directive('cmsMenuHeading', function () {
        return {
            scope: {
                section: '='
            },
            templateUrl: 'app/menu/menu-heading.tmpl.html'
        };
    })