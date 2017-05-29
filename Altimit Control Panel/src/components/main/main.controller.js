angular.module('altimitCPanel').controller('MainCtrl', ['$scope', '$http', '$timeout', function ($scope, $http, $timeout) {
  var me = this;

  //Prevent from calling after server is off line.
  me.makeCalls = true;

  me.callServer = function () {
    //Room count request
    $http.get('http://localhost:4567/roomCount')
      .then(function (response) {
        me.roomCount = response.data;
      }, function () {
        me.roomCount = "N/A";
      });

    //User count request
    $http.get('http://localhost:4567/userCount')
      .then(function (response) {
        me.userCount = response.data;
      }, function () {
        me.userCount = "N/A";
      });

    //Server status
    $http.get('http://localhost:4567/serverStatus')
      .then(function (response) {
        me.serverStatus = true;
      }, function () {
        me.serverStatus = false;
        me.makeCalls = false;
      });
  };

  me.StartServer = function(){
    var req = {
      method: 'POST',
      url: 'http://localhost:4567/startServer',
      headers: {
        'Content-Type': undefined
      },
      data: {}
    };

    $http(req)
      .then(function () {
        console.log("Server Started");
      }), function () {
        console.log("Start Server Failed!");
    };
  }

  me.StopServer = function(){
    var req = {
      method: 'POST',
      url: 'http://localhost:4567/stopServer',
      headers: {
        'Content-Type': undefined
      },
      data: {}
    };

    $http(req)
      .then(function () {
        console.log("Server Stopped");
      }), function () {
        console.log("Stop Server Failed!");
    };
  }

  me.callServer();

  $timeout(function() {
    if (me.makeCalls){
      me.callServer();
    }
  }, 15000);

}]);
