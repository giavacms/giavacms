
angular.module('giavacms-resources')

    .directive("fileread", [function () {
        return {
            scope: {
                fileread: "="
            },
            link: function (scope, element, attributes) {
                element.bind("change", function (changeEvent) {
                    var reader = new FileReader();
                    reader.onload = function (loadEvent) {
                        scope.$apply(function () {
                            scope.fileread['fileContent'] = loadEvent.target.result;
                        });
                    }
                    scope.fileread['name'] = changeEvent.target.files[0].name;
                    scope.fileread['size'] = changeEvent.target.files[0].size;
                    reader.readAsDataURL(changeEvent.target.files[0]);
                });
            }
        }
    }])

    .directive("multifileread", function () {
        return {
            scope: {
                multifileread: "="
            },
            link: function (scope, element, attributes) {
                element.bind("change", function (changeEvent) {
                    var readers = [] ,
                    files = changeEvent.target.files ,
                    datas = [] ;
                    for ( var i = 0 ; i < files.length ; i++ ) {
                        readers[ i ] = new FileReader();
                        readers[ i ].onload = function (loadEvent) {
                            datas.push( loadEvent.target.result );
                            if ( datas.length === files.length ){
                                scope.$apply(function () {
                                    for ( var i = 0 ; i < datas.length ; i++ ) {
                                        scope.multifileread[i]['fileContent'] = datas[i];
                                    }
                                });
                            }
                        }
                        if ( scope.multifileread.length <= i ) {
                            scope.multifileread.push({});
                        }
                        scope.multifileread[i]['name'] = changeEvent.target.files[i].name;
                        scope.multifileread[i]['size'] = changeEvent.target.files[i].size;
                        readers[ i ].readAsDataURL( files[i] );
                    }
                });
            }
        }
    })
    ;

//And the input tag becomes:
//<input type="file" fileread="vm.uploadme" />