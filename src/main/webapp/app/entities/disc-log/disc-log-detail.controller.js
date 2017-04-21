(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .controller('DiscLogDetailController', DiscLogDetailController);

    DiscLogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DiscLog'];

    function DiscLogDetailController($scope, $rootScope, $stateParams, previousState, entity, DiscLog) {
        var vm = this;

        vm.discLog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('deviceManagerApp:discLogUpdate', function(event, result) {
            vm.discLog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
