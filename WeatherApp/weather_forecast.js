var app = angular.module('myApp', []);

app.controller('formController', function($scope,$http) {
    $scope.showMe = false;
    
    // Obtain weather data for the city name entered
    $scope.submit = function() {
        $http.get("http://api.openweathermap.org/data/2.5/forecast/daily?q="+$scope.user.CityName+"&units=metric&cnt=7&appid=46c578a4bf13218db8f9f9d063375c66").then(function (response) {
        $scope.myData = response.data.list;
        $scope.latitude = response.data.city.coord.lat;
        $scope.longitude= response.data.city.coord.lon;
        $scope.City = response.data.city.name;
        $scope.Country = response.data.city.country;
        $scope.showMe = !$scope.showMe;
        $scope.date = new Date();
        $scope.weatherIcon = response.data.list.weather[0].description;
  });}
    
    // Reset input field values
    $scope.master = {CityName:""};
    $scope.reset = function() {
        $scope.user = angular.copy($scope.master);
        $scope.showMe = false;
    };
    $scope.reset();
});


//filter to capitalize the value of weather condition
app.filter('capitalize', function() {
    return function(input) {
      return (!!input) ? input.charAt(0).toUpperCase() + input.substr(1).toLowerCase() : '';
    }
});
