'use strict';

angular.module('giavacms-banner')

    .service('BannertypeService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'bannertypes'))

    })
;