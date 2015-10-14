'use strict';

angular.module('giavacms-home')

    .config(function ($stateProvider, $urlRouterProvider, BASE) {

        // Create a state for our seed test page
        $stateProvider.state(BASE + 'home', {
            // set the url of this page
            url: '/home',
            // set the html template to show on this page
            templateUrl: 'app/home/home.html',
            // set the controller to load for this page
            controller: 'HomeController'
        });

        // set default routes when no path specified
        $urlRouterProvider.when('', '/home');
        $urlRouterProvider.when('/', '/home');

    })

    .run(function(MenuService, BASE) {
        MenuService.addLink(BASE + "home", 'Home', 0, 'fa fa-home');
    })

    .controller('HomeController', function ($scope) {

    })

;
