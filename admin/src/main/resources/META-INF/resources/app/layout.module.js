'use strict';

angular.module(
        'giavacms-layout', [
        'ui.router',
        'giavacms-menu'
    ])

    /**
    * LAYOUT
    */
    .config(function ($stateProvider, $urlRouterProvider, APP) {

        $stateProvider

            .state('default', {
                cache: false,
                abstract: true,
                templateUrl: 'app/layout/layout.html',
                controller: 'LayoutController'
            })

            .state('default.layout', {
                cache: false,
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

            .state('404', {
                url: '/404',
                templateUrl: 'app/layout/404.html',
                controller: function ($scope, $state, APP) {
                    $scope.app = APP;

                    $scope.goHome = function () {
                        $state.go('admin.default.home');
                    };
                }
            })

            .state('500', {
                url: '/500',
                templateUrl: 'app/layout/500.html',
                controller: function ($scope, $state, APP) {
                    $scope.app = APP;

                    $scope.goHome = function () {
                        $state.go('panel.default.dashboard-analytics');
                    };
                }
            })

        ;

        // always goto 404 if route not found
        $urlRouterProvider.otherwise('/404');
        // till we are not ready for login and the whole thing
        //$urlRouterProvider.otherwise('/layout-test');

   })

    .config(function(APP) {
        APP.BASE = 'default.layout.';
    })


;
