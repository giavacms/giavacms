'use strict';

angular.module('giavacms-styles', ['ui.router'])

    .config(function(APP) {
        APP.STYLES = {
            PATH: APP.RESOURCES + 'scripts'
        };
    })

    .run(function(MenuService, APP) {

        var aclName = 'STYLES';
        var aclRoles = 'Admin, Developer, Resources';
        APP.ACL[aclName] = aclRoles;

        //APP.STYLES.toggle = MenuService.addToggleWithACL(aclName, 'styles',1300,'fa fa-css3');
    })


;