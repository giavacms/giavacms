'use strict';

angular.module('giavacms-security')

    .service('RolesService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'roles'))

    })
;