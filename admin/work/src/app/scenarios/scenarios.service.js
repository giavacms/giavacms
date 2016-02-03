'use strict';

angular.module('giavacms-scenario')

    .service('ScenariosService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'scenarios'))

    })
;