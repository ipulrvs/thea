(function() {
    'use strict';
    angular
        .module('monolithApp')
        .factory('TodoItem', TodoItem);

    TodoItem.$inject = ['$resource', 'DateUtils'];

    function TodoItem ($resource, DateUtils) {
        var resourceUrl =  'api/todo-items/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.endDate = DateUtils.convertLocalDateFromServer(data.endDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.endDate = DateUtils.convertLocalDateToServer(copy.endDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.endDate = DateUtils.convertLocalDateToServer(copy.endDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
