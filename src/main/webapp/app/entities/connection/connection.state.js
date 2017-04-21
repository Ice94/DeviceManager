(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('connection', {
            parent: 'entity',
            url: '/connection',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Connections'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/connection/connections.html',
                    controller: 'ConnectionController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('connection-detail', {
            parent: 'connection',
            url: '/connection/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Connection'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/connection/connection-detail.html',
                    controller: 'ConnectionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Connection', function($stateParams, Connection) {
                    return Connection.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'connection',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('connection-detail.edit', {
            parent: 'connection-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/connection/connection-dialog.html',
                    controller: 'ConnectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Connection', function(Connection) {
                            return Connection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('connection.new', {
            parent: 'connection',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/connection/connection-dialog.html',
                    controller: 'ConnectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userhost: null,
                                password: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('connection', null, { reload: 'connection' });
                }, function() {
                    $state.go('connection');
                });
            }]
        })
        .state('connection.edit', {
            parent: 'connection',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/connection/connection-dialog.html',
                    controller: 'ConnectionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Connection', function(Connection) {
                            return Connection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('connection', null, { reload: 'connection' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('connection.delete', {
            parent: 'connection',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/connection/connection-delete-dialog.html',
                    controller: 'ConnectionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Connection', function(Connection) {
                            return Connection.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('connection', null, { reload: 'connection' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
