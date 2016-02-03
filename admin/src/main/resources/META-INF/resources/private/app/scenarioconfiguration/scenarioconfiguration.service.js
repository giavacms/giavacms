'use strict';

angular.module('giavacms-scenario')

    .service('ScenarioconfigurationService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'scenarioconfiguration'))

    })
;