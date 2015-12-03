'use strict';

angular.module('giavacms-test')

    .config(function ($stateProvider, $urlRouterProvider, APP) {

        $stateProvider

            .state(APP.BASE + 'test1', {
                url: '/test/1',
                templateUrl: '/app/test/test1.html',
                controller: function($scope) {
                    $scope.test = "test1";
                }
            })

            .state(APP.BASE + 'test2', {
                url: '/test/2',
                templateUrl: '/app/test/test2.html',
                controller: function($scope) {
                    $scope.test = "test2";
                }
            })

    })

    .run(function(MenuService, APP) {
        if ( APP.TEST ) {
            MenuService.addHeading('',9000);
            MenuService.addHeading('',9900);
            MenuService.addHeading('',9990);

            MenuService.addHeading('test header',9991,'fa fa-file-text');
            var toggle = MenuService.addToggle('test toggle',9992,'fa fa-book');
            MenuService.addLink(APP.BASE + "test1", 'test one', 1, 'fa fa-bookmark',toggle);
            MenuService.addLink(APP.BASE + "test2", 'test two', 2, 'fa fa-bookmark-o',toggle);
            MenuService.addLink(APP.BASE + "test1", 'test one again', 9993, 'fa fa-user');
            MenuService.addHeading('test header2',9994,'fa fa-file');

            var aclName = 'LAYOUT_TEST';
            var aclRoles = 'Admin, admin, Developer, LayoutTest';
            APP.ACL[aclName] = aclRoles;

            MenuService.addHeadingWithACL(aclName,'test ACL header',9995,'fa fa-file-text');
            var toggleAcl = MenuService.addToggleWithACL(aclName,'test ACL toggle',9996,'fa fa-book');
            MenuService.addLinkWithACL(aclName, APP.BASE + "test1", 'test ACL one', 1, 'fa fa-bookmark',toggleAcl);
        }
    })

;