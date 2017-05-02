(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .controller('DiscDetailController', DiscDetailController);

    DiscDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Disc', 'Connection'];

    function DiscDetailController($scope, $rootScope, $stateParams, previousState, entity, Disc, Connection) {
        var vm = this;

        vm.disc = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('deviceManagerApp:discUpdate', function(event, result) {
            vm.disc = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
