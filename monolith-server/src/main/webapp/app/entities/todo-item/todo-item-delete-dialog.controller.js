(function() {
    'use strict';

    angular
        .module('monolithApp')
        .controller('TodoItemDeleteController',TodoItemDeleteController);

    TodoItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'TodoItem'];

    function TodoItemDeleteController($uibModalInstance, entity, TodoItem) {
        var vm = this;

        vm.todoItem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TodoItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
