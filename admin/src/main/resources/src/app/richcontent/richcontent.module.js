'use strict';

angular.module('giavacms-richcontent', ['ui.router'])

    .config(function(APP) {
        APP.RICHCONTENT = {};
    })

    .run(function(MenuService, APP) {

        var aclName = 'RICHCONTENT';
        var aclRoles = 'Admin, Developer, Richcontent';
        APP.ACL[aclName] = aclRoles;

        APP.RICHCONTENT.toggle = MenuService.addToggleWithACL(aclName, 'richcontent',2100,'fa fa-book');
    })

;
