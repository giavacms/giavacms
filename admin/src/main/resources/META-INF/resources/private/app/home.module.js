'use strict';

angular.module('giavacms-home', ['ui.router', 'giavacms-menu'])

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state(APP.BASE + 'home', {
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

    .run(function (MenuService, APP) {
        MenuService.addLink(APP.BASE + "home", 'Home', 0, 'fa fa-home');
        MenuService.addHeadingWithACL('ADMIN', 'Settings', 0);
        MenuService.addHeading('Commodities', 1000);
        MenuService.addHeading('Installed modules', 2000);
    });

