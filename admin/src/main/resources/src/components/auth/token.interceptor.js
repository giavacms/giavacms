'use strict';


angular.module('giavacms-auth')

    .config(function ($httpProvider, jwtInterceptorProvider) {

        jwtInterceptorProvider.tokenGetter = function (StorageService) {
            return StorageService.get('token');
        }

        $httpProvider.interceptors.push('jwtInterceptor');
    })

;

