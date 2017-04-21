(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('disc', {
            parent: 'entity',
            url: '/disc',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Discs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/disc/discs.html',
                    controller: 'DiscController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('disc-detail', {
            parent: 'disc',
            url: '/disc/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Disc'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/disc/disc-detail.html',
                    controller: 'DiscDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Disc', function($stateParams, Disc) {
                    return Disc.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'disc',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('disc-detail.edit', {
            parent: 'disc-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disc/disc-dialog.html',
                    controller: 'DiscDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Disc', function(Disc) {
                            return Disc.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('disc.new', {
            parent: 'disc',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disc/disc-dialog.html',
                    controller: 'DiscDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('disc', null, { reload: 'disc' });
                }, function() {
                    $state.go('disc');
                });
            }]
        })
        .state('disc.edit', {
            parent: 'disc',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disc/disc-dialog.html',
                    controller: 'DiscDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Disc', function(Disc) {
                            return Disc.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('disc', null, { reload: 'disc' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('disc.delete', {
            parent: 'disc',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disc/disc-delete-dialog.html',
                    controller: 'DiscDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Disc', function(Disc) {
                            return Disc.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('disc', null, { reload: 'disc' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
