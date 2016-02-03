'use strict';

angular.module('giavacms-pages', ['ui.router'])

    .config(function(APP) {
        APP.PAGES = {
            PATH: '/'
        };
    })

    .run(function(MenuService, APP) {

        var aclName = 'PAGES';
        var aclRoles = 'Admin, admin, Developer, Resources';
        APP.ACL[aclName] = aclRoles;

        //APP.PAGES.toggle = MenuService.addToggleWithACL(aclName, 'pages',1500,'fa fa-html5');
    })


;