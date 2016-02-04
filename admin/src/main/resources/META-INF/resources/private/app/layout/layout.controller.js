'use strict';

angular.module('giavacms-layout')

    .controller('LayoutController', function($mdBottomSheet, $mdSidenav, $q, $scope, $timeout, APP, AuthenticationService, MenuService) {

        $scope.appLogo = APP.LOGO;
        $scope.appName = APP.NAME;
        /**
        * This may turn useful for inner views
        */
        $scope.user = {};
        AuthenticationService.getUser().then(function (user) {
            $scope.user = user;
            $scope.logged = user;
        });
        $scope.permit = AuthenticationService.permit;

        /**
         * First hide the bottomsheet IF visible, then
         * hide or Show the 'left' sideNav area
         */
        $scope.toggleSidebarLeft = function() {
          var pending = $mdBottomSheet.hide() || $q.when(true);

          pending.then(function(){
            $mdSidenav('left').toggle();
          });
        }

        $scope.menu = MenuService.getMenu();

        $scope.isSectionSelected = function(section) {
            return section && section.toState == $scope.menu.current.toState;
        }

        $scope.isOpen = function(section) {
            return section.open;
        }

        $scope.toggleOpen = function(section) {
            return section.state = $scope.page.state;
        }

		$scope.clock = Date.now(); //"loading clock..."; // initialise the time variable
		$scope.tickInterval = 1000 // ms

		var tick = function() {
			$scope.clock = Date.now() // get the current time
			$timeout(tick, $scope.tickInterval); // reset the timer
		}

		// Start the timer
		$timeout(tick, $scope.tickInterval);

    })

;
