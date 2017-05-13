(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .controller('DiscDetailController', DiscDetailController);

    DiscDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Disc', 'Connection', 'DiscLog'];

    function DiscDetailController($scope, $rootScope, $stateParams, previousState, entity, Disc, Connection, DiscLog) {
        var vm = this;
        vm.discLogs = [];

        vm.disc = entity;
        vm.previousState = previousState.name;
        loadDiscLogs();
        console.log(vm.discLogs);
        function loadDiscLogs() {
            vm.discLogs = DiscLog.getLogs({id : $stateParams.id});
            vm.discLogs.$promise.then(function (response) {
                vm.discLogs = response;
            });
        }
        var unsubscribe = $rootScope.$on('deviceManagerApp:discUpdate', function(event, result) {
            vm.disc = result;
        });
        $scope.$on('$destroy', unsubscribe);
        $scope.labels = ["January", "February", "March", "April", "May", "June", "July"];
        $scope.series = ['Series A', 'Series B'];
        $scope.data = [
            [65, 59, 80, 81, 56, 55, 40],
            [28, 48, 40, 19, 86, 27, 90]
        ];
        $scope.datasetOverride = [{
            yAxisID: 'y-axis-1'
        }, {
            yAxisID: 'y-axis-2'
        }];
        $scope.options = {
            scales: {
                yAxes: [{
                    id: 'y-axis-1',
                    type: 'linear',
                    display: true,
                    position: 'left'
                }, {
                    id: 'y-axis-2',
                    type: 'linear',
                    display: true,
                    position: 'right'
                }]
            }
        };
        $scope.labels1 = ['2006', '2007', '2008', '2009', '2010', '2011', '2012'];
        $scope.series1 = ['Series A', 'Series B'];

        $scope.data1 = [
            [65, 59, 80, 81, 56, 55, 40],
            [28, 48, 40, 19, 86, 27, 90]
        ];
    }
})();
