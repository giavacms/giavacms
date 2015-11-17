'use strict';

angular.module('giavacms-documents', ['ui.router'])

    .config(function(APP) {
        APP.DOCUMENTS = {};
    })

    .run(function(MenuService, APP) {

        var aclName = 'DOCUMENTS';
        var aclRoles = 'Admin, Developer, Resources';
        APP.ACL[aclName] = aclRoles;

        //APP.DOCUMENTS.toggle = MenuService.addToggleWithACL(aclName, 'documents',1500,'fa fa-file');
    })


;