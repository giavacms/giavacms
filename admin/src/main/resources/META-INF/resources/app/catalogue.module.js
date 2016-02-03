'use strict';

angular.module('giavacms-catalogue', ['ui.router', 'giavacms-table'])

    .config(function(APP) {
        APP.CATALOGUE = {
            IMAGESPATH: APP.RESOURCES + 'images'
        };
    })

    .run(function(MenuService, APP) {

        var aclName = 'CATALOGUE';
        var aclRoles = 'Admin, admin, Developer, Catalogue';
        APP.ACL[aclName] = aclRoles;

        APP.CATALOGUE.toggle = MenuService.addToggleWithACL(aclName, 'catalogue',2300,'fa fa-book');

    })

;
