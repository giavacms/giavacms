'use strict';

angular.module('giavacms-graphics', ['ui.router'])

    .config(function(APP) {
        APP.GRAPHICS = {};
    })

    .run(function(MenuService, APP) {

        var aclName = 'GRAPHICS';
        var aclRoles = 'Admin, Developer, Resources';
        APP.ACL[aclName] = aclRoles;

        //APP.GRAPHICS.toggle = MenuService.addToggleWithACL(aclName, 'graphics',1100,'fa fa-picture-o');
    })


;