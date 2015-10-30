'use strict';

angular.module('giavacms-richcontent')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'richcontent', {
            // set the url of this page
            url: '/richcontent',
            // set the html template to show on this page
            templateUrl: 'app/richcontent/richcontent-list.html',
            // set the controller to load for this page
            controller: 'RichcontentListController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "richcontent", 'Richcontent list', 1, 'fa fa-list', APP.RICHCONTENT.toggle);
    })

    .controller('RichcontentListController', function ($scope) {

    })

;
