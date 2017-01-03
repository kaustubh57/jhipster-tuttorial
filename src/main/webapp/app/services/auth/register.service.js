(function () {
    'use strict';

    angular
        .module('jhipstertutorialApp')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
