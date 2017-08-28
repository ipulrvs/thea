(function() {
    'use strict';

    angular
        .module('monolithApp')
        .controller('TodoItemDetailController', TodoItemDetailController);

    TodoItemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TodoItem', 'Todo'];

    function TodoItemDetailController($scope, $rootScope, $stateParams, previousState, entity, TodoItem, Todo) {
        var vm = this;

        vm.todoItem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('monolithApp:todoItemUpdate', function(event, result) {
            vm.todoItem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
