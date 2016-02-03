'use strict';

angular.module('giavacms-menu')

    .directive('cmsMenuToggle', [ '$timeout',
        function($timeout) {
            return {
                scope: {
                    section: '='
                },
                templateUrl: 'app/menu/menu-toggle.tmpl.html',
                link: function($scope, $element) {
                    $scope.isOpen = function() {
                        return $scope.section.open;
                    };
                    $scope.toggle = function() {
                        if ($scope.section.open) {
                            $scope.section.open = false;
                        }
                        else {
                            $scope.section.open = true;
                        }
                    };
                }
            };
        }]);

