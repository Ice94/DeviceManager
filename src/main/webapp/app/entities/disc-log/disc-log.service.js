(function() {
    'use strict';
    angular
        .module('deviceManagerApp')
        .factory('DiscLog', DiscLog);

    DiscLog.$inject = ['$resource', 'DateUtils'];

    function DiscLog ($resource, DateUtils) {
        var resourceUrl =  'api/disc-logs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
