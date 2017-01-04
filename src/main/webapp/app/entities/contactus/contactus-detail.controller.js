(function() {
    'use strict';

    angular
        .module('jhipstertutorialApp')
        .controller('ContactusDetailController', ContactusDetailController);

    ContactusDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Contactus'];

    function ContactusDetailController($scope, $rootScope, $stateParams, previousState, entity, Contactus) {
        var vm = this;

        vm.contactus = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipstertutorialApp:contactusUpdate', function(event, result) {
            vm.contactus = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
