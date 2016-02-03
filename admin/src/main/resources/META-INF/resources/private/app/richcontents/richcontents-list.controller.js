'use strict';

angular.module('giavacms-richcontents')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'richcontents_list', {
            // set the url of this page
            url: '/richcontents',
            // set the html template to show on this page
            templateUrl: 'app/richcontents/richcontents-list.html',
            // set the controller to load for this page
            controller: 'RichcontentsListController'
        });

        // Create a state for our seed test page
        $stateProvider.state( APP.BASE + 'richcontents_view', {
            // set the url of this page
            url: '/richcontents',
            // set the html template to show on this page
            templateUrl: 'app/richcontents/richcontents-list.html',
            // set the controller to load for this page
            controller: 'RichcontentsListController'
        });

    })

    .run(function(MenuService, APP) {
        MenuService.addLink(APP.BASE + "richcontents_list", 'RichContent list', 30, 'fa fa-list', APP.RICHCONTENTS.toggle);
    })

    .controller('RichcontentsListController', function ($filter, $log, $mdDialog, $q, $scope, $state, APP, RichcontentsService, RichcontenttypesService) {

        var headers = [
        //            {field: 'id', label: 'identificativo', sortable: true},
            {field: 'highlight', label: '', sortable: false},
            {field: 'title', label: 'title', sortable: true},
            {field: 'richContentType', label: 'richcontent type', sortable: false},
            {field: 'date', label: 'date', sortable: true},
        ];

        var overrides = {
            pageSize: 5
        };

        RsListController($filter, $log, $mdDialog, $q, $scope, $state, APP, RichcontentsService, headers, overrides);

        $scope.richContentTypes = [];

        var initRichContentTypes = function () {
            return RichcontenttypesService.getList({}, 0, 0).then(
                function (richContentTypes) {
                    $scope.richContentTypes = richContentTypes;
                    return $q.when(true);
                }
            );
        }

        initRichContentTypes();

    });
;
