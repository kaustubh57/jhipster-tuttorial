(function() {
    'use strict';

    angular
        .module('jhipstertutorialApp')
        .controller('ContactusController', ContactusController);

    ContactusController.$inject = ['$scope', '$state', 'Contactus'];

    function ContactusController ($scope, $state, Contactus) {
        var vm = this;

        vm.contactus = [];

        loadAll();

        function loadAll() {
            Contactus.query(function(result) {
                vm.contactus = result;
                vm.searchQuery = null;
            });
        }
    }
})();
