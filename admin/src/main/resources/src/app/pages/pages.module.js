'use strict';

angular.module('giavacms-pages', ['ui.router'])

    .config(function(APP) {
        APP.PAGES = {};
    })

    .run(function(MenuService, APP) {

        var aclName = 'PAGES';
        var aclRoles = 'Admin, Developer, Resources';
        APP.ACL[aclName] = aclRoles;

        //APP.PAGES.toggle = MenuService.addToggleWithACL(aclName, 'pages',1400,'fa fa-html5');
    })


;