'use strict';

angular.module('giavacms-menu')

    .directive('cmsMenuLink', function () {
        return {
            scope: {
                section: '=',
                page: '='
            },
            templateUrl: 'app/menu/menu-link.tmpl.html',
            link: function ($scope, $element) {
                var s = $scope.section;
                var p = $scope.page;
                $scope.focusSection = function () {
                    // set flag to be used later when
                    // $locationChangeSuccess calls openPage()
                    //controller.autoFocusContent = true;
                };
            }
        };
    })