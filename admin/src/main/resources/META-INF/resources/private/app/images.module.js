'use strict';

angular.module('giavacms-images', ['ui.router'])

    .config(function (APP) {
        APP.IMAGES = {
            PATH: APP.RESOURCES + 'images'
        };
    })

    .run(function (MenuService, APP) {

        var aclName = 'IMAGES';
        var aclRoles = 'Admin, admin, Developer, Resources';
        APP.ACL[aclName] = aclRoles;

        //APP.IMAGES.toggle = MenuService.addToggleWithACL(aclName, 'images',1100,'fa fa-picture-o');
    });
