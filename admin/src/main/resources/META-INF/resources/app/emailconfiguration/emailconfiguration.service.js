'use strict';

angular.module('giavacms-security')

    .service('EmailconfigurationService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'emailconfiguration'))

    })
;