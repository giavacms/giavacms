'use strict';

angular.module('giavacms-security')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'emailconfiguration', {
            // set the url of this page
            url: '/emailconfiguration',
            // set the html template to show on this page
            templateUrl: 'app/emailconfiguration/emailconfiguration.html',
            // set the controller to load for this page
            controller: 'EmailconfigurationController'
        });

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'emailconfiguration_view', {
            // set the url of this page
            url: '/emailconfiguration',
            // set the html template to show on this page
            templateUrl: 'app/emailconfiguration/emailconfiguration.html',
            // set the controller to load for this page
            controller: 'EmailconfigurationController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLinkWithACL('EMAILCONFIGURATION', APP.BASE + "emailconfiguration", 'EMAIL CONFIGURATION', 300, 'fa fa-envelope');
    })

    .controller('EmailconfigurationController', function ($filter, $log, $mdDialog, $q, $scope, $state, $stateParams, APP, EmailconfigurationService) {

        var utf8_to_b64 = function(str) {
            return window.btoa(unescape(encodeURIComponent(str)));
        }

        var b64_to_utf8 = function(str) {
            return decodeURIComponent(escape(window.atob(str)));
        }

        $scope.element = {};

        var overrides = {
            preUpdate: function() {
                $scope.element.password = utf8_to_b64($scope.element.password);
                return $q.when(true);
            }
        };

        $stateParams.id = 1;
        var editFunction = RsEditController($log, $mdDialog, $q, $scope, $state, $stateParams, APP, EmailconfigurationService, overrides);

        editFunction.init().then(
            function() {
                if ( $scope.element.password ) {
                    $scope.element.password = b64_to_utf8($scope.element.password);
                }
            }
        );

    })

;
