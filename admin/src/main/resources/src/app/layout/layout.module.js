'use strict';

angular.module('giavacms-layout', [
'ui.router'
])

/**
* LAYOUT
*/
    .config(function ($stateProvider, $urlRouterProvider) {

        $stateProvider

            .state('default', {
                abstract: true,
                templateUrl: 'app/layout/default.html',
                controller: 'LayoutController'
            })

            .state('default.layout', {
                abstract: true,
                views: {
                    sidebarLeft: {
                        // potrebbe governare l'elemento attivo contenuto in qualche service
                        // controller: 'SidebarLeftController',
                        templateUrl: 'app/layout/sidebar-left.html'
                    },
                    toolbar: {
                        // potrebbe governare il titolo della pagina corrente e la sezione profilo dell'utente loggato
                        // controller: 'DefaultToolbarController',
                        templateUrl: 'app/layout/toolbar.html'
                    },
                    content: {
                        template: '<div flex ui-view></div>'
                    }
                }
            })

.state('root', {
                url: '/',
                templateUrl: 'itworks.html',
});

	 $stateProvider
           .state('404', {
                url: '/404',
                templateUrl: '404.html',
                controller: function ($scope, $state, APP) {
                    $scope.app = APP;

                    $scope.goHome = function () {
                        $state.go('admin.default.home');
                    };
                }
            })

            .state('500', {
                url: '/500',
                templateUrl: '500.html',
                controller: function ($scope, $state, APP) {
                    $scope.app = APP;

                    $scope.goHome = function () {
                        $state.go('panel.default.dashboard-analytics');
                    };
                }
            })

        // always goto 404 if route not found
        $urlRouterProvider.otherwise('/404');

   })

;
