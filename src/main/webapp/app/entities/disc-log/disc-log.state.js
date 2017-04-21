(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('disc-log', {
            parent: 'entity',
            url: '/disc-log',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DiscLogs'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/disc-log/disc-logs.html',
                    controller: 'DiscLogController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('disc-log-detail', {
            parent: 'disc-log',
            url: '/disc-log/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'DiscLog'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/disc-log/disc-log-detail.html',
                    controller: 'DiscLogDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'DiscLog', function($stateParams, DiscLog) {
                    return DiscLog.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'disc-log',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('disc-log-detail.edit', {
            parent: 'disc-log-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disc-log/disc-log-dialog.html',
                    controller: 'DiscLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DiscLog', function(DiscLog) {
                            return DiscLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('disc-log.new', {
            parent: 'disc-log',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disc-log/disc-log-dialog.html',
                    controller: 'DiscLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                util: null,
                                svctim: null,
                                await: null,
                                avgqusz: null,
                                avgrqsz: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('disc-log', null, { reload: 'disc-log' });
                }, function() {
                    $state.go('disc-log');
                });
            }]
        })
        .state('disc-log.edit', {
            parent: 'disc-log',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disc-log/disc-log-dialog.html',
                    controller: 'DiscLogDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['DiscLog', function(DiscLog) {
                            return DiscLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('disc-log', null, { reload: 'disc-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('disc-log.delete', {
            parent: 'disc-log',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/disc-log/disc-log-delete-dialog.html',
                    controller: 'DiscLogDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['DiscLog', function(DiscLog) {
                            return DiscLog.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('disc-log', null, { reload: 'disc-log' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
