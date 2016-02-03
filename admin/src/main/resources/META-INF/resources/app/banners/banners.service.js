'use strict';

angular.module('giavacms-banners')

    .service('BannersService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'banners'))

    })
;