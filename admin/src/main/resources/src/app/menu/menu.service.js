'use strict';



/**
see https://material.angularjs.org/0.9.6/#/api/material.components.sidenav/service/$mdSidenav

forse non qui ma direttamente nel controller...
*/

angular.module('giavacms-menu')

    .factory('MenuService',
	    function ($q, $rootScope, AuthenticationService) {

			var items = 0;

	        var menu = {
	        	sections: [],
	        	current: {}
	        }

			$rootScope.$on('$stateChangeSuccess', function(event, toState, toParams, fromState, fromParams){
				menu.sections.forEach(function(section) {
					if ( section.toState && section.toState == toState.name ) {
						menu.current = section;
					}
					else if ( section.children ) {
						section.children.forEach(function(child) {
							if ( child.toState && child.toState == toState.name ) {
								menu.current = child;
							}
						});
					}
				});
			});

			var getMenu = function() {
				return menu;
			}

			var checkACL = function(acl) {
				if ( !acl ) {
            		return $q.when(true);
				}
				else {
					return AuthenticationService.isLogged().then(
						function(loggedIn) {
							if ( !loggedIn ) {
								// unauthorized access prevention comes with the login module.
								// otherwise no check is necessary
			            		// return $q.when(false);
			            		return $q.when(true);
							}
							else if ( ! AuthenticationService.permit(acl) ) {
			            		return $q.when(false);
							}
	                		else {
			            		return $q.when(true);
	                		}
						}
					);
				}
			}

			var addSection = function(menuItem) {
				checkACL(menuItem.acl).then(
					function(aclOk) {
						if ( !aclOk ) {
							menuItem.hidden = true
							// return;
						}
						if ( angular.isUndefined( menuItem['position'] ) ) {
							menuItem['position'] = 1;
						}
						if ( menu.sections.length == 0 ) {
							menu.sections.push(menuItem);
						}
						else {
							var added = false;
							for ( var index = 0; index < menu.sections.length; index++ ) {
								if ( menu.sections[index].position > menuItem.position ) {
									  menu.sections.splice(index, 0, menuItem);
									  added = true;
									  break;
								}
							}
							if ( ! added ) {
								menu.sections.push(menuItem);
							}
						}
					}
				);
			}

			var addChildren = function(parent, menuItem) {
				checkACL(menuItem.acl).then(
					function(aclOk) {
						if ( !aclOk ) {
							menuItem.hidden = true
							// return;
						}
						if ( ! menuItem['position'] ) {
							menuItem['position'] = 1;
						}
						if ( parent.children.length == 0 ) {
							parent.children.push(menuItem);
						}
						else {
							var added = false;
							for ( var index = 0; index < parent.children.length; index++ ) {
								if ( parent.children[index].position > menuItem.position ) {
									  parent.children.splice(index, 0, menuItem);
									  added = true;
									  break;
								}
							}
							if ( ! added ) {
								parent.children.push(menuItem);
							}
						}
					}
				);
			}

			var addHeading = function(name, position, icon, parentItem) {
				addHeadingWithACL(null, name, position, icon, parentItem);
			}

			var addHeadingWithACL = function(acl, name, position, icon, parentItem) {
				var headerMenuItem = {
					itemId: ++items,
					acl: acl,
					name: name,
					type: 'heading',
					position: position,
					icon: icon
				};
				if ( parentItem ) {
					addChildren(parentItem, headerMenuItem);
				}
				else {
					addSection(headerMenuItem);
				}
			}

			var addToggle = function(name, position, icon, parentItem) {
				return addToggleWithACL(null,name,position,icon,parentItem);
			}

			var addToggleWithACL = function(acl, name, position, icon, parentItem) {
				var toggleMenuItem = {
					itemId: ++items,
					acl: acl,
					name: name,
					type: 'toggle',
					position: position,
					icon: icon,
					children: []
				};
				if ( parentItem ) {
					addChildren(parentItem, toggleMenuItem);
				}
				else {
					addSection(toggleMenuItem);
				}
				return toggleMenuItem;
			}

			var addLink = function(toState, name, position, icon, parentItem) {
				addLinkWithACL(null,toState,name,position,icon, parentItem);
			}

			var addLinkWithACL = function(acl, toState, name, position, icon, parentItem) {
				var linkMenuItem = {
					itemId: ++items,
					acl: acl,
					name: name,
					type: 'link',
					icon: icon,
					toState: toState,
					position: position
				};
				if ( parentItem ) {
					addChildren(parentItem, linkMenuItem);
				}
				else {
					addSection(linkMenuItem);
				}
			}

			var recheck = function() {
				menu.sections.forEach(
					function(item) {
						recheckSection(item);
						if ( item.children ) {
							item.children.forEach(
								function(child) {
									recheckSection(child);
								}
							);
						}
					}
				);
			}

			var recheckSection = function(section) {
				if ( section.acl ) {
					checkACL(section.acl).then(
						function(aclOk) {
							if ( !aclOk ) {
								section.hidden = true
							}
							else {
								delete section.hidden;
							}
						}
					);
				}
			}

			var service = {
				addHeadingWithACL: addHeadingWithACL,
				addToggleWithACL: addToggleWithACL,
				addLinkWithACL: addLinkWithACL,
				addHeading: addHeading,
				addToggle: addToggle,
				addLink: addLink,
				getMenu: getMenu,
				recheck: recheck
			}

			return service;

		})
;


