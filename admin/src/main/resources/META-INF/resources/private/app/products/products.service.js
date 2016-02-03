'use strict';

angular.module('giavacms-catalogue')

    .service('ProductsService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'products'))

    })
;