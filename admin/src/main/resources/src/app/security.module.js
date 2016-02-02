'use strict';

angular.module('giavacms-security', ['ui.router'])

    .config(function(APP) {
        APP.USERS = {
        };
    })

    .run(function(MenuService, APP) {

        var aclName = 'ACCOUNTS';
        var aclRoles = 'Admin, admin, Developer';
        APP.ACL[aclName] = aclRoles;

        var aclName = 'EMAILCONFIGURATION';
        var aclRoles = 'admin, Developer';
        APP.ACL[aclName] = aclRoles;

        APP.USERS.toggle = MenuService.addToggleWithACL(aclName, 'users', 200, 'fa fa-group');
    })

;
