<#macro login>
<#if !user.authenticated>
<button type="button" class="btn btn-default navbar-btn" id="login_google">Sign in with Google</button>
<#else>
<p class="navbar-text">${user.name}</p></li>
<li><a href="#" id="logout">Log out</a>
</#if>
</#macro>
