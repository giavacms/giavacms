'use strict';

angular.module('giavacms-scenario', ['ui.router', 'giavacms-table'])

    .config(function (APP) {
        APP.SCENARIO = {
            IMAGESPATH: APP.RESOURCES + 'images'
        };
    })

    .run(function (MenuService, APP) {

        var aclName = 'SCENARIO';
        var aclRoles = 'Admin, admin, Developer, Scenario';
        APP.ACL[aclName] = aclRoles;

        APP.SCENARIO.toggle = MenuService.addToggleWithACL(aclName, 'scenario', 2400, 'fa fa-flag');

    });
