'use strict';


angular.module('giavacms-auth')
    .factory('AuthenticationService',
        function ($http, $log, $q, $rootScope, $timeout, APP) {

            var host = APP.HOST;
            var context = APP.CONTEXT;
            var acl = APP.ACL;

            var user = undefined;

            var isLogged = function () {
                console.log('isLogged');
                if (user) {
                    return $q.when(true);
                } else {
                    return login().then(function (data) {
                        if (data) {
                            return $q.when(true);
                        }
                        else {
                            return $q.when(false);
                        }
                    })
                }
                return $q.when(false);
            };

            var login = function () {
                console.log('login');
                var aboutMe = APP.PROTOCOL + '://' + APP.HOST + (APP.CONTEXT ? APP.CONTEXT : '') + '/api/v1/users/aboutMe';
                return $http.get(aboutMe).then(function (response) {
                    user = response.data;
                    return user;
                }, function errorCallback(response) {
                    console.log('ERRORE IN FASE DI AUTENTICAZIONE!!');
                    return;
                });
            };

            var logout = function () {
                user = {};
            };


            var getRoles = function () {
                return user.roles;
            };

            var permit = function (toAuthorizes) {
                console.log('permit: ' + toAuthorizes);
                if (toAuthorizes === undefined) {
                    return true;
                }
                if (!user) {
                    return false;
                }

                var roles = getRoles();
                var toAuthorizeArray = toAuthorizes.split(',');
                for (var a = 0; a < toAuthorizeArray.length; a++) {
                    var toAuthorize = toAuthorizeArray[a].trim();
                    if (!acl[toAuthorize]) {
                        $log.warn('unknown ACL entry: ' + toAuthorize);
                    }
                    else {
                        for (var r = 0; r < roles.length; r++) {
                            if (acl[toAuthorize].indexOf(roles[r]) > -1) {
                                $log.debug('user ' + user.username + ' authorized to ' + toAuthorize + ' as ' + roles[r]);
                                return true;
                            }
                        }
                    }
                }
                $log.debug('user ' + user.username + ' unauthorized to ' + toAuthorizes);
                return false;
            };


            var service = {
                getFullname: function () {
                    return user.name;
                },
                getUsername: function () {
                    return user.username;
                },
                getUser: function () {
                    if (user) {
                        return $q.when(user);
                    } else {
                        return login();
                    }
                },
                getRoles: getRoles,
                isLogged: isLogged,
                login: login,
                logout: logout,
                permit: permit
            }

            return service;

        });
