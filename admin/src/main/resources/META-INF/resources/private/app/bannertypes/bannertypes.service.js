'use strict';

angular.module('giavacms-banners')

    .service('BannertypesService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'bannertypes'))

    })
;