(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .controller('DiscController', DiscController);

    DiscController.$inject = ['$scope', '$state', 'Disc'];

    function DiscController ($scope, $state, Disc) {
        var vm = this;

        vm.discs = [];

        loadAll();

        function loadAll() {
            Disc.query(function(result) {
                vm.discs = result;
                vm.searchQuery = null;
            });
        }
    }
})();
