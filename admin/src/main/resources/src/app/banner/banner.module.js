'use strict';

angular.module('giavacms-banner', ['ui.router'])

    .config(function(APP) {
        APP.BANNER = {};
    })

    .run(function(MenuService, APP) {

        var aclName = 'BANNER';
        var aclRoles = 'Admin, Developer, Banner';
        APP.ACL[aclName] = aclRoles;

        APP.BANNER.toggle = MenuService.addToggleWithACL(aclName, 'banner',2200,'fa fa-book');
    })

;
