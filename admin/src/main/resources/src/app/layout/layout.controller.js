'use strict';

angular.module('giavacms-layout')

    .config(function ($stateProvider, $urlRouterProvider) {

        $stateProvider

            .state('default.layout.test1', {
                url: '/layout-test1',
                templateUrl: '/app/layout/layout-test1.html',
                controller: function($scope) {
                    $scope.test = "layout-test1";
                }
            })

            .state('default.layout.test2', {
                url: '/layout-test2',
                templateUrl: '/app/layout/layout-test2.html',
                controller: function($scope) {
                    $scope.test = "layout-test2";
                }
            })

    })

    .run(function(MenuService, BASE) {
        var test = true;
        if ( test ) {
            MenuService.addHeading('test header',1,'fa fa-file-text');
            var toggle = MenuService.addToggle('test toggle',2,'fa fa-book');
            MenuService.addLink(BASE + "test1", 'test one', 1, 'fa fa-bookmark',toggle);
            MenuService.addLink(BASE + "test2", 'test two', 2, 'fa fa-bookmark-o',toggle);
            MenuService.addLink(BASE + "test3", 'test three', 1, 'fa fa-user',toggle);
            MenuService.addHeading('test header2',3,'fa fa-file');
        }
    })

    .controller('LayoutController', function($mdBottomSheet, $mdSidenav, $q, $scope, $timeout, AuthenticationService, MenuService) {

        /**
        * This may turn useful for inner views
        */
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