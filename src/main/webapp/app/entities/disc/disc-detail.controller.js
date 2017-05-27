(function() {
    'use strict';

    angular
        .module('deviceManagerApp')
        .controller('DiscDetailController', DiscDetailController);

    DiscDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Disc', 'Connection', 'DiscLog','$interval'];

    function DiscDetailController($scope, $rootScope, $stateParams, previousState, entity, Disc, Connection, DiscLog, $interval) {
        var vm = this;
        vm.discLogs = [];
        vm.discLogsChartData = [];

        var dataReloadInterval = 60 * 1000;

        vm.disc = entity;

        vm.previousState = previousState.name;
        loadDiscLogs();
        console.log(vm.discLogs);
        function loadDiscLogs() {
            vm.discLogs = DiscLog.getLogs({id : $stateParams.id});
            vm.discLogs.$promise.then(function (response) {
                vm.discLogs = response;
                repackChartData(vm.discLogs);
            });
        }
        var unsubscribe = $rootScope.$on('deviceManagerApp:discUpdate', function(event, result) {
            vm.disc = result;
        });
        var interval = $interval(function () {
            loadDiscLogs();
        }, dataReloadInterval);

        $scope.series = ['svctm', 'await', 'avgqusz', 'avgrqsz','util'];

        $scope.options = {
            legend: {"display": true}
        };

        $scope.colors = [
            {
                backgroundColor: '#E062ff',
                pointBackgroundColor: '#E062ff',
                pointHoverBackgroundColor: '#E062ff',
                borderColor: '#E062ff',
                pointBorderColor: '#E062ff',
                pointHoverBorderColor: '#E062ff',
                fill: false
            },
            {
                backgroundColor : '#A072ff',
                pointBackgroundColor: '#A072ff',
                pointHoverBackgroundColor: '#A072ff',
                borderColor: '#A072ff',
                pointBorderColor: '#A072ff',
                pointHoverBorderColor: '#A072ff',
                fill: false
            },

            {
                backgroundColor : '#0382ff',
                pointBackgroundColor: '#0382ff',
                pointHoverBackgroundColor: '#0382ff',
                borderColor: '#0382ff',
                pointBorderColor: '#0382ff',
                pointHoverBorderColor: '#0382ff',
                fill: false
            },

            {
                backgroundColor : '#3092ff',
                pointBackgroundColor: '#3092ff',
                pointHoverBackgroundColor: '#3092ff',
                borderColor: '#3092ff',
                pointBorderColor: '#3092ff',
                pointHoverBorderColor: '#3092ff',
                fill: false
            }
        ];


        var repackChartData = function (discLogData) {
            var chartLabels = [];
            var chartData = [
                [], // svctim
                [], // await
                [], // avgqusz
                [], // avgrqsz
                [] // util
            ];
            angular.forEach(discLogData, function (discLog, i) {
                console.log("Disc log [" + i + "] " + JSON.stringify(discLog));
                chartData[0].push(discLog.svctim);
                chartData[1].push(discLog.await);
                chartData[2].push(discLog.avgqusz);
                chartData[3].push(discLog.avgrqsz);
                chartData[4].push(discLog.util);
                chartLabels.push(moment(discLog.date).format('LLL'));
            });

            $scope.labels = chartLabels;
            $scope.data = chartData;

         //   console.log($scope.labels);
            console.log($scope.data);
        };
    }
})();
