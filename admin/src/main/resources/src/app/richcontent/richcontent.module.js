'use strict';

angular.module('giavacms-richcontent', ['ui.router'])

    .config(function ($stateProvider, $urlRouterProvider) {

var BASE;

        // Create a state for our seed test page
        $stateProvider.state(BASE + 'richcontent', {
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

    .run(function(LayoutService, HOME) {
        var test = true;
        if ( test ) {
            var header = LayoutService.addHeader('','test header','fa fa-group',1);
            var toggle = LayoutService.addToggle('test toggle','fa fa-map-marker',1);
            var link = LayoutService.addLink('test link','fa fa-plus',1, toggle);
        }
    })


/*

DO THIS BY MANAGING THE LEFT SIDEBAR 

    .run(function (SideMenu, HOME) {
        // add a menu for the seed page we created in the $stateProvider above
        SideMenu.addMenu({
            // give the menu a name to show (should be translatable and in the il8n
            // folder json)
            name: 'Pagina di benvenuto',
            // point this menu to the state we created in the $stateProvider above
            state: HOME,
            // set the menu type to a link
            type: 'link',
            // set an icon for this menu
            icon: 'icon-home',
            // set who can see this page
            permit: 'ADMIN,INSERT',
            // set a proirity for this menu item, menu is sorted by priority
            priority: 1
        });
    })

*/
;
