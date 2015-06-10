<#include 'header.ftl'>
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
    </div>
</div>
<#include 'footer.ftl'>
