<!doctype html>
<html ng-app>
  <head>
    <script type="text/javascript" src="../libs/angular.min.js"></script>
    <script type="text/javascript" src="../libs/ui-bootstrap-tpls-0.10.0.js"></script>
  </head>
  <body>
  	<!-- Adding the ng-app declaration to initialize AngularJS -->
	<div id="main" ng-app>
		<div ng-include="'../templates/header.html'"></div>
		<div ng-view></div>
		<div ng-include="'../templates/footer.html'"></div>
	</div>
  </body>
</html>