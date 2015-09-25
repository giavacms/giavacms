'use strict';



/**
see https://material.angularjs.org/0.9.6/#/api/material.components.sidenav/service/$mdSidenav

forse non qui ma direttamente nel controller...
*/

angular.module('giavacms-layout')

    .factory('LayoutService',
    function ($http, $log, $q, $rootScope, $timeout, ACL, APP_CONST, QuizConsultazioniService) {

        var menu = {};
	var page = {};

	var getPage = function() {
		return page;
	}
	var setPage = function(p) {
		page = p;
	}

	var getMenu = function() {
		return menu;
	}

	var addToMenu = function(menuitem, position) {
		// TODO;
	}

        var service = {
            getMenu: getMenu,
            addToMenu: addToMenu,
            getPage: getPage,
            setPage: setPage
    }

        return service;

    })
;


