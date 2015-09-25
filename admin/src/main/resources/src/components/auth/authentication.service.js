'use strict';


angular.module('giavacms-auth')

    .factory('AuthenticationService',
    function ($http, $log, $q, $rootScope, $timeout, ACL, APP_CONST, jwtHelper, StorageService, HOME) {

        var host = APP_CONST.HOST;
        var context = APP_CONST.CONTEXT;

        StorageService.get('info').then(
            function (info) {
                if (info && info.length > 0) {
                    if (info[0].host) {
                        host = info[0].host;
                    }
                    if (info[0].context) {
                        context = info[0].context;
                    }
                }
            }
        );

        var logged;
        var tokenPayload = {};
        var token = {};

        var isLogged = function () {
            if (logged) {
                return $q.when(logged);
            }

            return StorageService.get('token').then(function (storedToken) {
                if (storedToken && storedToken.length > 0) {
                    token = storedToken[0];
                    if (token && token.toString().indexOf(".") > -1) {
                        tokenPayload = jwtHelper.decodeToken(token);
                        identify(tokenPayload.username);
                        logged = true;
                    }
                    else {
                        logged = false;
                    }
                }
                else {
                    logged = false;
                }
                return logged;
            });
        };

        var login = function (user) {
            var url = '//' + host + '/' + context + '/api/login';
            $http.post(url, {
                username: user.username,
                password: user.password
            }).then(
                function success(data, status, headers, config) {
                    // this callback will be called asynchronously
                    // when the response is available
                    $log.debug(data);
                    token = data.data.token;
                    tokenPayload = jwtHelper.decodeToken(token);
                    logged = true;
                    var storedToken = [];
                    storedToken.push(token);
                    StorageService.set('token', storedToken).then(
                        identify(user.username).then(
                            function success(ok) {
                                if (ok) {
                                    $rootScope.$broadcast('login-confirmed', status);
                                }
                                else {
                                    $rootScope.$broadcast('login-failed');
                                }
                            },
                            function failuer() {
                                $rootScope.$broadcast('login-failed');
                            }
                        )
                    );
                },
                function error(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $log.debug(data);
                    $rootScope.$broadcast('login-failed');
                });
        };

        var identify = function (username) {
            var url = '//' + host + '/' + context + '/api/v1/identita/' + username;
            return $http.get(url).then(
                function success(data, status, headers, config) {
                    // this callback will be called asynchronously
                    // when the response is available
                    $log.debug(data);
                    tokenPayload.name = data.data.cognome + ' ' + data.data.nome;
                    return $q.when(true);
                },
                function error(data, status, headers, config) {
                    // called asynchronously if an error occurs
                    // or server returns response with an error status.
                    $log.debug(data);
                    return $q.when(false);
                });
        }

        var logout = function (user) {
            logged = false;
            token = {};
            tokenPayload = {};
            StorageService.set('token', '').then(function () {
                $rootScope.$broadcast('logout-complete');
            });
            //StorageService.set('token', []);

        };

        var loginCancelled = function () {
            $rootScope.$broadcast('login-failed');
        };

        var getRoles = function () {
            var roles = tokenPayload.roles ? tokenPayload.roles : [];
            roles.push('Any');
            return roles;
        };

        var permit = function (toAuthorizes) {
            if (toAuthorizes === undefined) {
                return true;
            }
            if (!tokenPayload || !tokenPayload.username) {
                return false;
            }
            var roles = getRoles();
            var toAuthorizeArray = toAuthorizes.split(',');
            for (var a = 0; a < toAuthorizeArray.length; a++) {
                var toAuthorize = toAuthorizeArray[a].trim();
                if (!ACL[toAuthorize]) {
                    $log.warn('unknown ACL entry: ' + toAuthorize);
                }
                else {
                    for (var r = 0; r < roles.length; r++) {
                        if (ACL[toAuthorize].indexOf(roles[r]) > -1) {
                            $log.debug('user ' + tokenPayload.username + ' authorized to ' + toAuthorize + ' as ' + roles[r]);
                            return true;
                        }
                    }
                }
            }
            $log.debug('user ' + tokenPayload.username + ' unauthorized to ' + toAuthorizes);
            return false;
        };


        var service = {

            getToken: function () {
                return token;
            },
            getFullname: function () {
                return tokenPayload.name;
            },
            getUsername: function () {
                return tokenPayload.username;
            },
            getExpiryDate: function () {
                return tokenPayload.exp;
            },
            getRoles: getRoles,
            isLogged: isLogged,
            login: login,
            logout: logout,
            loginCancelled: loginCancelled,
            permit: permit

        }

        return service;

    })


