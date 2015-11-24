'use strict';

angular.module('giavacms-richcontents', ['ui.router'])

    .config(function(APP) {
        APP.RICHCONTENTS = {};
    })

    .run(function(MenuService, APP) {

        var aclName = 'RICHCONTENTS';
        var aclRoles = 'Admin, Developer, Richcontents';
        APP.ACL[aclName] = aclRoles;

        APP.RICHCONTENTS.toggle = MenuService.addToggleWithACL(aclName, 'richcontents', 2100, 'fa fa-language');
    })

;
