'use strict';

angular.module('giavacms-resources', ['ui.router'])

    .config(function(APP) {
        APP.RESOURCES = {};
    })

    .run(function(MenuService, APP) {

        var aclName = 'RESOURCES';
        var aclRoles = 'Admin, Developer, Resources';
        APP.ACL[aclName] = aclRoles;

        APP.RESOURCES.toggle = MenuService.addToggleWithACL(aclName, 'resources',1100,'fa fa-picture');
    })


;