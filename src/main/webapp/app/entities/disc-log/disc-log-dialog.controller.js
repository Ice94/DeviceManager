(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .controller('DiscLogDialogController', DiscLogDialogController);

    DiscLogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DiscLog'];

    function DiscLogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DiscLog) {
        var vm = this;

        vm.discLog = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.discLog.id !== null) {
                DiscLog.update(vm.discLog, onSaveSuccess, onSaveError);
            } else {
                DiscLog.save(vm.discLog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('deviceManagerApp:discLogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
