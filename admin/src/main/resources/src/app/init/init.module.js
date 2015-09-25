'use strict';

angular.module('giavacms-init', [
'LocalStorageModule'
])

/**
* CONSTANTS
*/

    .constant('APP', {
        name: 'GiavaCms Administration Panel',
        logo: 'assets/images/logo.png',
        version: '1.0.0'
    })
    .constant('APP_CONST', {
        'HOST': 'localhost:8080',
        'CONTEXT': ''
    })
    .constant('ACL', {
        ANY: ['Any'],
        DEVELOPER: ['Developer'],
        ADMIN: ['Admin']
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


;
