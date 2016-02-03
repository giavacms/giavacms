'use strict';

angular.module('giavacms-richcontents')

    .service('RichcontenttypesService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'richcontenttypes'))

    })
;