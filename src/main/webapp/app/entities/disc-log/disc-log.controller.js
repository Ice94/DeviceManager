(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .controller('DiscLogController', DiscLogController);

    DiscLogController.$inject = ['$scope', '$state', 'DiscLog'];

    function DiscLogController ($scope, $state, DiscLog) {
        var vm = this;

        vm.discLogs = [];

        loadAll();

        function loadAll() {
            DiscLog.query(function(result) {
                vm.discLogs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
