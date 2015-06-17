<#include 'header.ftl'>
<#include 'footer.ftl'>
<@header/>
<div class="container">
  <div class="content">
      <form name="form" action="/login" method="POST">
        <fieldset>
          <input type="text" name="username" value="" placeholder="Username" />
          <input type="password" name="password" placeholder="Password" />
        </fieldset>
        <input type="submit" id="login" value="Login" class="btn btn-primary" />
      </form>
   <!--   <button id="frog">g login</button> -->
    </div>
</div>
<@footer>
<!--
<script>
$('#frog').on('click', function() {
  Hull.login({provider:'google'}).then(onSuccess, onError);
});

var onSuccess = function(user) {
  console.log("Welcome", user.name);
};

var onError = function(error) {
  console.log("oops, something went wrong : ", error.reason);
}

</script> -->
</@footer>
