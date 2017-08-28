(function() {
    'use strict';

    angular
        .module('monolithApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('todo-item', {
            parent: 'entity',
            url: '/todo-item?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'monolithApp.todoItem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/todo-item/todo-items.html',
                    controller: 'TodoItemController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('todoItem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('todo-item-detail', {
            parent: 'todo-item',
            url: '/todo-item/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'monolithApp.todoItem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/todo-item/todo-item-detail.html',
                    controller: 'TodoItemDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('todoItem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TodoItem', function($stateParams, TodoItem) {
                    return TodoItem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'todo-item',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('todo-item-detail.edit', {
            parent: 'todo-item-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/todo-item/todo-item-dialog.html',
                    controller: 'TodoItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TodoItem', function(TodoItem) {
                            return TodoItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('todo-item.new', {
            parent: 'todo-item',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/todo-item/todo-item-dialog.html',
                    controller: 'TodoItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                status: null,
                                description: null,
                                cost: null,
                                endDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('todo-item', null, { reload: 'todo-item' });
                }, function() {
                    $state.go('todo-item');
                });
            }]
        })
        .state('todo-item.edit', {
            parent: 'todo-item',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/todo-item/todo-item-dialog.html',
                    controller: 'TodoItemDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TodoItem', function(TodoItem) {
                            return TodoItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('todo-item', null, { reload: 'todo-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('todo-item.delete', {
            parent: 'todo-item',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/todo-item/todo-item-delete-dialog.html',
                    controller: 'TodoItemDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TodoItem', function(TodoItem) {
                            return TodoItem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('todo-item', null, { reload: 'todo-item' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
