'use strict';

angular.module('giavacms-richcontents')

    .service('RichcontentsService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'richcontents'))

    })
;