'use strict';

angular.module('giavacms-catalogue')

    .service('CategoriesService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'categories'))

    })
;