'use strict';

angular.module('giavacms-test', [
'ui.router',
'giavacms-layout'
'giavacms-auth'
])

    .config(function ($stateProvider, $urlRouterProvider) {

        $stateProvider

            .state('default.layout.test', {
                url: '/test',
                templateUrl: 'app/test/test.html',
                controller: 'TestController'
            })

   })

    .run(function ($rootScope, $state, AuthenticationService, LayoutService) {

// if ( AuthenticationService.permit('TEST') {
var test = {};
LayoutService.addToMenu(test);
// }
});

;
