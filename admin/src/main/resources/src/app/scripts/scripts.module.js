'use strict';

angular.module('giavacms-scripts', ['ui.router'])

    .config(function(APP) {
        APP.SCRIPTS = {
            PATH: APP.RESOURCES + 'scripts'
        };
    })

    .run(function(MenuService, APP) {

        var aclName = 'SCRIPTS';
        var aclRoles = 'Admin, admin, Developer, Resources';
        APP.ACL[aclName] = aclRoles;

        //APP.SCRIPTS.toggle = MenuService.addToggleWithACL(aclName, 'scripts',1200,'fa fa-cogs');
    })


;