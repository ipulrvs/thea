(function() {
    'use strict';

    angular
        .module('monolithApp')
        .controller('TodoItemDialogController', TodoItemDialogController);

    TodoItemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TodoItem', 'Todo'];

    function TodoItemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TodoItem, Todo) {
        var vm = this;

        vm.todoItem = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.todos = Todo.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.todoItem.id !== null) {
                TodoItem.update(vm.todoItem, onSaveSuccess, onSaveError);
            } else {
                TodoItem.save(vm.todoItem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('monolithApp:todoItemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.endDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
