'use strict';

angular.module('giavacms-rs')

    .factory('RsResource', function ($resource) {

        return $resource('//:host:context/api/v1/:entityType/:id/:entityType2/:id2', {
            host: '@host',
            context: '@context',
            entityType: '@entityType',
            id: '@id',
            entityType2: '@entityType2',
            id2: '@id2'
        }, {
            query: {method: 'GET', isArray: true},
            create: {method: 'POST'},
            show: {method: 'GET'},
            update: {method: 'PUT', params: {id: '@id'}},
            delete: {method: 'DELETE', params: {id: '@id'}}
        })
    });
