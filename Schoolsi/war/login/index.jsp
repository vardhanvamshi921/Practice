<!doctype html>
<html ng-app>
  <head>
  	<link href="//netdna.bootstrapcdn.com/bootstrap/3.0.3/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/style.css">
    <script type="text/javascript" src="../libs/angular.min.js"></script>
    <script type="text/javascript" src="../libs/ui-bootstrap-tpls-0.10.0.js"></script>
  </head>
  <body>
  	<!-- Adding the ng-app declaration to initialize AngularJS -->
	<div id="main" ng-app>
	  <div ng-include="'../templates/header.html'"></div>
	  <div class=page-content>
		 	 <div class="register">
			 	 <h2 class="login-title">Login</h2>
			 	 <form >
				    <input type="text" class="register-input" placeholder="User id">
				    <input type="password" class="register-input" placeholder="Password">
				    <input type="submit" value="Siginin" class="register-button">
			    </form>
			    <div id="links_right"><a href="#">Forgot password?</a></div>
		   </div> 
	  </div>
	<div ng-include="'../templates/footer.html'"></div>
	</div>
  </body>
</html>