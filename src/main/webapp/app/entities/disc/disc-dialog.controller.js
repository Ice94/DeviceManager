(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .controller('DiscDialogController', DiscDialogController);

    DiscDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Disc', 'Connection'];

    function DiscDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Disc, Connection) {
        var vm = this;

        vm.disc = entity;
        vm.clear = clear;
        vm.save = save;
        vm.connections = Connection.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.disc.id !== null) {
                Disc.update(vm.disc, onSaveSuccess, onSaveError);
            } else {
                Disc.save(vm.disc, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('deviceManagerApp:discUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
