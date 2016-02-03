'use strict';

angular.module('giavacms-catalogue')

    .service('CatalogueconfigurationService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'catalogueconfiguration'))

    })
;