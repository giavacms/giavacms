angular.module('giavacms-auth')

    .directive('permit', function ($animate, AuthenticationService) {
        return {
            multiElement: true,
            //transclude: true, // full DOM
            transclude: 'element', // just the current element
            priority: 1,
            restrict: 'A',
            $$tlb: true,
            // scope: {}, // dal controller qui sotto non accedo al parent $scope.element
            //scope: true, // dal controller qui sotto accedo a ... una copia? di parent $scope.element
            //scope: false, // differenza con true non capita...
            /*
             controller: function ($scope) {
             console.error('element id is: ' + $scope.element['id']);
             $scope.element.id = 99999; // questa modifica non sembra avere effetto
             console.error('element id changed to: ' + $scope.element['id']);
             },
             */
            link: function (scope, element, attr, ctrl, $transclude) {

                // mi aggiungo qui e mi rimuovo da qui
                var block, childScope, previousElements;

                var hideOrShow = function (value) {

                    // se ok mi aggiungo
                    if (AuthenticationService.permit(value)) {
                        // scope.element.id = 99999; // questa modifica ha effetto subito sullo scope esterno, ma poi viene piallata. boh.
                        if (!childScope) {
                            $transclude(function (clone, newScope) {
                                childScope = newScope;
                                clone[clone.length++] = document.createComment(' end ngIf: ' + attr.ngIf + ' ');
                                // Note: We only need the first/last node of the cloned nodes.
                                // However, we need to keep the reference to the jqlite wrapper as it might be changed later
                                // by a directive with templateUrl when its template arrives.
                                block = {
                                    clone: clone
                                };
                                $animate.enter(clone, element.parent(), element);
                            });
                        }
                    }

                    // se non ok e mi sono aggiunto mi tolgo
                    else {
                        if (previousElements) {
                            previousElements.remove();
                            previousElements = null;
                        }
                        if (childScope) {
                            childScope.$destroy();
                            childScope = null;
                        }
                        if (block) {
                            previousElements = getBlockNodes(block.clone);
                            $animate.leave(previousElements).then(function () {
                                previousElements = null;
                            });
                            block = null;
                        }
                    }
                }

                if (false) {
                    // this expects <div permit="'ACL_ENTRY1,ACL_ENTRY2'">....</div>
                    //scope.$watch(attr.permit, hideOrShow);
                    hideOrShow(scope.$eval(attr.permit));
                }
                if (false) {
                    // this expects <div permit="ACL_ENTRY1,ACL_ENTRY2">....</div>
                    /*
                     scope.$watch(function () {
                     return attr.permit;
                     }, hideOrShow);
                     */
                    hideOrShow(scope.$eval(function () {
                        return attr.permit;
                    }));
                }
                if (false) {
                    attr.$observe('permit', function (value) {
                        console.log("observe direttiva = " + value)
                    });
                }
                if (false) {
                    scope.$watch(attr.permit, function (value) {
                        console.log("watch direttiva = " + value)
                    });
                }
                if (true) {
                    var aclEntry = scope.$eval(attr.permit);
                    //scope.$watch(attr.permit, function (aclEntry) {
                    console.log("eval direttiva = " + aclEntry);
                    if (aclEntry == undefined || AuthenticationService.permit(aclEntry.toString())) {
                        $transclude(function (clone, newScope) {
                            childScope = newScope;
                            clone[clone.length++] = document.createComment(' end ngIf: ' + attr.ngIf + ' ');
                            // Note: We only need the first/last node of the cloned nodes.
                            // However, we need to keep the reference to the jqlite wrapper as it might be changed later
                            // by a directive with templateUrl when its template arrives.
                            block = {
                                clone: clone
                            };
                            $animate.enter(clone, element.parent(), element);
                        });
                    }
                    //});
                }

            }
        }
    })


    .
    directive('myDirectiveEval', function ($parse) {
        return {
            //$$tlb: true,
            link: function (scope, element, attr) {
                console.log("eval = " + scope.$eval(attr.topermit));
                /*
                 scope.$watch(attr.topermit, function (value) {
                 if (value) {
                 console.log("watch = " + value);
                 }
                 });
                 */
            }
        }
    })

    .directive('myDirectiveObserve', function ($animate, AuthenticationService) {
        return {
            $$tlb: true,
            transclude: 'element', // just the current element
            link: function (scope, element, attr, ctrl, $transclude) {
                attr.$observe('myDirectiveObserve', function (value) {
                    //  console.log("observe = " + value);
                    if (value === undefined || AuthenticationService.permit(value)) {
                        $transclude(function (clone, newScope) {
                            clone[clone.length++] = document.createComment(' end permit ');
                            // Note: We only need the first/last node of the cloned nodes.
                            // However, we need to keep the reference to the jqlite wrapper as it might be changed later
                            // by a directive with templateUrl when its template arrives.

                            $animate.enter(clone, element.parent(), element);
                        });
                    }
                })
            }
        }
    })
