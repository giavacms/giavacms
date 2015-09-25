'use strict';

function RsService(APP_CONST, RsResource, entityType) {

    var host = APP_CONST.HOST;
    var context = APP_CONST.CONTEXT;

    var list = [];
    var size = 0;

    var getList = function (search, startRow, pageSize, orderBy, id, entityType2) {
        var reqParams = {};
        reqParams['host'] = host;
        reqParams['context'] = context;
        reqParams['entityType'] = entityType;
        reqParams['id'] = id;
        reqParams['entityType2'] = entityType2;
        reqParams['startRow'] = startRow;
        reqParams['pageSize'] = pageSize;
        if (orderBy) {
            reqParams['orderBy'] = orderBy;
        }
        angular.forEach(search, function (value, key) {
            reqParams[key] = value;
        });
        return RsResource.query(reqParams, function (data, headers) {
            list = data;
            size = headers('listSize');
            return data;
        }).$promise;
    }

    var getElement = function (id) {
        var reqParams = {};
        reqParams['host'] = host;
        reqParams['context'] = context;
        reqParams['entityType'] = entityType;
        reqParams['id'] = id;
        return RsResource.get(reqParams, function (data) {
            return data;
        }).$promise;
    }

    var save = function (toTransfer) {
        var reqParams = {};
        reqParams['host'] = host;
        reqParams['context'] = context;
        reqParams['entityType'] = entityType;
        return RsResource.create(reqParams, toTransfer, function (data) {
            return data;
        }).$promise;
    }

    var update = function (toTransfer, id) {
        var reqParams = {};
        reqParams['host'] = host;
        reqParams['context'] = context;
        reqParams['entityType'] = entityType;
        if (id) {
            reqParams['id'] = id;
        }
        return RsResource.update(reqParams, toTransfer, function (data) {
            return data;
        }).$promise;
    }

    var remove = function (toRemove, id) {
        var reqParams = {};
        reqParams['host'] = host;
        reqParams['context'] = context;
        reqParams['entityType'] = entityType;
        if (id) {
            reqParams['id'] = id;
        }
        return RsResource.delete(reqParams, toRemove, function (data) {
            return data;
        }).$promise;
    }

    var getSize = function () {
        return size;
    }

    return {
        getList: getList,
        getSize: getSize,
        getElement: getElement,
        save: save,
        update: update,
        delete: remove,
        entityType: entityType
    };

};
