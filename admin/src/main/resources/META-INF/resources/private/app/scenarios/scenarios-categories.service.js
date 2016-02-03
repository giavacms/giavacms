'use strict';

angular.module('giavacms-scenario')

    .service('ScenariosCategoriesService', function (APP, RsResource) {

        angular.extend(this, new RsService(APP, RsResource, 'categories'))

    })
;