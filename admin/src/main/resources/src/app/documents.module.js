'use strict';

angular.module('giavacms-documents', ['ui.router'])

    .config(function(APP) {
        APP.DOCUMENTS = {
            PATH: APP.RESOURCES + 'documents'
        };
    })

    .run(function(MenuService, APP) {

        var aclName = 'DOCUMENTS';
        var aclRoles = 'Admin, admin, Developer, Resources';
        APP.ACL[aclName] = aclRoles;

        //APP.DOCUMENTS.toggle = MenuService.addToggleWithACL(aclName, 'documents',1400,'fa fa-file');
    })


;