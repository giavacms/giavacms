'use strict';

angular.module('giavacms-banner')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'banner', {
            // set the url of this page
            url: '/banner',
            // set the html template to show on this page
            templateUrl: 'app/banner/banner-list.html',
            // set the controller to load for this page
            controller: 'BannerListController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "banner", 'Banner list', 1, 'fa fa-list', APP.BANNER.toggle);
    })

    .controller('BannerListController', function ($scope) {

    })

;
