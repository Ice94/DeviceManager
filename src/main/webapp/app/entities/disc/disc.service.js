(function() {
    'use strict';
    angular
        .module('deviceManagerApp')
        .factory('Disc', Disc);

    Disc.$inject = ['$resource'];

    function Disc ($resource) {
        var resourceUrl =  'api/discs/:id';

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
