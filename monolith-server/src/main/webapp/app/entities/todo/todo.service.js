(function() {
    'use strict';
    angular
        .module('monolithApp')
        .factory('Todo', Todo);

    Todo.$inject = ['$resource', 'DateUtils'];

    function Todo ($resource, DateUtils) {
        var resourceUrl =  'api/todos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startDate = DateUtils.convertDateTimeFromServer(data.startDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
