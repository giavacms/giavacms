'use strict';

angular.module('giavacms-config', [
    'LocalStorageModule',
    'ui.router'
])

    /**
    * CONSTANTS
    */
    .constant('APP', {
        NAME: 'GiavaCms',
        LOGO: 'assets/images/logo.png',
        VERSION: '1.0.0',
        PROTOCOL: 'http',
        HOST: 'localhost:8080',
//      CONTEXT: '/yourctxhere'
//        CONTEXT: '/gestioneformazione',
        CONTEXT: '',
        TEST: true,
        ACL: {
            ANY: ['Any'],
            DEVELOPER: ['Developer'],
            ADMIN: ['Admin']
        },
        LANGUAGES: ['ENG','ITA']
    })

    /**
     *  SETUP TRANSLATIONS
     */
    .run(function () {
        moment.locale('it');
    })


    /**
    * LOCAL STORAGE
    */
    .config(function (localStorageServiceProvider) {
        // set prefix for local storage
        localStorageServiceProvider
            .setPrefix('giavacms')
            .setStorageType('sessionStorage');
    })

    /**
    * WINDOWS HACKS
    */
    .run(function ($rootScope, $window) {
        // add a class to the body if we are on windows
        if ($window.navigator.platform.indexOf('Win') !== -1) {
            $rootScope.bodyClasses = ['os-windows'];
        }
    })

    /**
    * SUPPORTED LANGUAGES AVAILABLE IN ALL PAGES
    */
    .run(function ($rootScope, APP) {
        $rootScope.languages = APP.LANGUAGES;
    })


    /**
    * SANITIZATION
    */
    .config(['$compileProvider', function ($compileProvider) {
        $compileProvider.imgSrcSanitizationWhitelist(/^\s*(https?|ftp|mailto|content|file|data):/);
    }])

    /**
    * LOG LEVEL
    */
    .config(['$logProvider', function ($logProvider) {
        $logProvider.debugEnabled(false);
    }])


    /**
    * TEST PAGE
    */
    .config(function ($stateProvider, $urlRouterProvider) {

        $stateProvider

            .state('config-test', {
                url: '/config-test',
                templateUrl: '/app/config/config-test.html',
                controller: function($scope) {
                    $scope.test = "config-test";
                }
            })

        ;

        // till we are not ready for layout thing
        //$urlRouterProvider.otherwise('/init-config');

    })



    /// State change errors aren't shown in the console normally. You will need to add an error listener like so in order to see what went wrong:
    .run(function ($rootScope, $log) {
        $rootScope.$on('$stateChangeError', function(event, toState, toParams, fromState, fromParams, error) {
            $log.error("$stateChangeError: ", toState, error);
        });
    })


    // Provides an empty layout base if no layout module will be loaded
    .config(function(APP) {
        if ( ! APP.BASE ) {
            APP.BASE = '';
        }
    })

;
