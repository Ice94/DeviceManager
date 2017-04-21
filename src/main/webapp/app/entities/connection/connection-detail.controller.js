(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .controller('ConnectionDetailController', ConnectionDetailController);

    ConnectionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Connection', 'Disc', 'User'];

    function ConnectionDetailController($scope, $rootScope, $stateParams, previousState, entity, Connection, Disc, User) {
        var vm = this;

        vm.connection = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('deviceManagerApp:connectionUpdate', function(event, result) {
            vm.connection = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
