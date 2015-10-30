'use strict';

angular.module('giavacms-catalogue', ['ui.router'])

    .config(function(APP) {
        APP.CATALOGUE = {};
    })

    .run(function(MenuService, APP) {

        var aclName = 'CATALOGUE';
        var aclRoles = 'Admin, Developer, Catalogue';
        APP.ACL[aclName] = aclRoles;

        APP.CATALOGUE.toggle = MenuService.addToggleWithACL(aclName, 'catalogue',2300,'fa fa-book');
    })

;
