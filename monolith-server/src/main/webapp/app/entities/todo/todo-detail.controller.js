(function() {
    'use strict';

    angular
        .module('monolithApp')
        .controller('TodoDetailController', TodoDetailController);

    TodoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Todo'];

    function TodoDetailController($scope, $rootScope, $stateParams, previousState, entity, Todo) {
        var vm = this;

        vm.todo = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('monolithApp:todoUpdate', function(event, result) {
            vm.todo = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
