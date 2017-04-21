(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .controller('DiscLogDeleteController',DiscLogDeleteController);

    DiscLogDeleteController.$inject = ['$uibModalInstance', 'entity', 'DiscLog'];

    function DiscLogDeleteController($uibModalInstance, entity, DiscLog) {
        var vm = this;

        vm.discLog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DiscLog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
