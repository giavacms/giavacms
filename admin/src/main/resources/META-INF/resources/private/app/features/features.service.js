'use strict';

angular.module('giavacms-catalogue')

    .service('FeaturesService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'features'))

    })
;