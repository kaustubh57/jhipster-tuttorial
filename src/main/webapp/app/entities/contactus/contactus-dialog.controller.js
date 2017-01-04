(function() {
    'use strict';

    angular
        .module('jhipstertutorialApp')
        .controller('ContactusDialogController', ContactusDialogController);

    ContactusDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Contactus'];

    function ContactusDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Contactus) {
        var vm = this;

        vm.contactus = entity;
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
            if (vm.contactus.id !== null) {
                Contactus.update(vm.contactus, onSaveSuccess, onSaveError);
            } else {
                Contactus.save(vm.contactus, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipstertutorialApp:contactusUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
