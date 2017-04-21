(function() {
    'use strict';
    angular
        .module('deviceManagerApp')
        .factory('Connection', Connection);

    Connection.$inject = ['$resource'];

    function Connection ($resource) {
        var resourceUrl =  'api/connections/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
