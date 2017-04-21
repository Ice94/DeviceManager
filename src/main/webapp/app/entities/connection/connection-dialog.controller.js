(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .controller('ConnectionDialogController', ConnectionDialogController);

    ConnectionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Connection', 'Disc', 'User'];

    function ConnectionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Connection, Disc, User) {
        var vm = this;

        vm.connection = entity;
        vm.clear = clear;
        vm.save = save;
        vm.discs = Disc.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.connection.id !== null) {
                Connection.update(vm.connection, onSaveSuccess, onSaveError);
            } else {
                Connection.save(vm.connection, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('deviceManagerApp:connectionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
