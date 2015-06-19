<#macro footer>
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
  <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.4/js/bootstrap.min.js"></script>
  <script src="https://d3f5pyioow99x0.cloudfront.net/0.8.46/hull.js"></script>
  <script type='text/javascript'>
  Hull.init({
    "appId": "557e82ec847e7dad0b001413",
    "orgUrl": "https://754701bd.hullapp.io"
  });
  </script>
  <#nested/>
  <script>
  <#if !user.authenticated>
  $('#login_google').on('click', function() {
    Hull.login({provider:'google'}).then(onSuccess, onError);
    event.preventDefault();
  });
  
  var onSuccess = function(user) {
    console.log("Welcome", user);
    location.reload();
  };
  
  var onError = function(error) {
    console.log("oops, something went wrong : ", error.reason);
  }
  </#if>
  $('#logout').on('click', function() {
    Hull.api('/logout', 'get').then(function(response) {
      console.log(response);
      window.location = '/';
    });
    event.preventDefault();
  });
  </script>
  </body>
</html>
</#macro>