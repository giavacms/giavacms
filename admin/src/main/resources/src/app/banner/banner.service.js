'use strict';

angular.module('giavacms-banner')

    .service('BannerService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'banners'))

    })
;