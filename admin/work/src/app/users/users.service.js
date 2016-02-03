'use strict';

angular.module('giavacms-security')

    .service('UsersService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'users'))

    })
;