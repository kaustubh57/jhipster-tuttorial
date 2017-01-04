(function() {
    'use strict';

    angular
        .module('jhipstertutorialApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('contactus', {
            parent: 'entity',
            url: '/contactus',
            data: {
                authorities: [],
                pageTitle: 'jhipstertutorialApp.contactus.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contactus/contactus.html',
                    controller: 'ContactusController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contactus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('contactus-detail', {
            parent: 'entity',
            url: '/contactus/{id}',
            data: {
                authorities: [],
                pageTitle: 'jhipstertutorialApp.contactus.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/contactus/contactus-detail.html',
                    controller: 'ContactusDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('contactus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Contactus', function($stateParams, Contactus) {
                    return Contactus.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'contactus',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('contactus-detail.edit', {
            parent: 'contactus-detail',
            url: '/detail/edit',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contactus/contactus-dialog.html',
                    controller: 'ContactusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Contactus', function(Contactus) {
                            return Contactus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contactus.new', {
            parent: 'contactus',
            url: '/new',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contactus/contactus-dialog.html',
                    controller: 'ContactusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                contactname: null,
                                email: null,
                                message: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('contactus', null, { reload: 'contactus' });
                }, function() {
                    $state.go('contactus');
                });
            }]
        })
        .state('contactus.edit', {
            parent: 'contactus',
            url: '/{id}/edit',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contactus/contactus-dialog.html',
                    controller: 'ContactusDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Contactus', function(Contactus) {
                            return Contactus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contactus', null, { reload: 'contactus' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('contactus.delete', {
            parent: 'contactus',
            url: '/{id}/delete',
            data: {
                authorities: []
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/contactus/contactus-delete-dialog.html',
                    controller: 'ContactusDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Contactus', function(Contactus) {
                            return Contactus.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('contactus', null, { reload: 'contactus' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
