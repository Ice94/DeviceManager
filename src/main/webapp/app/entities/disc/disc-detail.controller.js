(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .controller('DiscDetailController', DiscDetailController);

    DiscDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Disc', 'Connection', 'DiscLog'];

    function DiscDetailController($scope, $rootScope, $stateParams, previousState, entity, Disc, Connection, DiscLog) {
        var vm = this;
        vm.discLogs = [];

        vm.disc = entity;
        vm.previousState = previousState.name;
        loadDiscLogs();
        console.log(vm.discLogs);
        function loadDiscLogs() {
            vm.discLogs = DiscLog.getLogs({id : $stateParams.id});
            vm.discLogs.$promise.then(function (response) {
                vm.discLogs = response;
            });
        }
        var unsubscribe = $rootScope.$on('deviceManagerApp:discUpdate', function(event, result) {
            vm.disc = result;
        });

    }
})();
