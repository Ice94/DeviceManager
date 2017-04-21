(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .controller('DiscDeleteController',DiscDeleteController);

    DiscDeleteController.$inject = ['$uibModalInstance', 'entity', 'Disc'];

    function DiscDeleteController($uibModalInstance, entity, Disc) {
        var vm = this;

        vm.disc = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Disc.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
