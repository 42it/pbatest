<#include 'header.ftl'>
<@header/>
<div class="container">
  <h1>Here will be a list of awesome videos</h1>
  <div class="row">
      <#list videos as video>
      <div class="col-xs-6 col-md-3">
        <a href="/play/${video.identifier}" class="thumbnail">
          <img src="/images/randalf_pad.webp" alt="${video.description}">
          <div class="caption">
            <h6>${video.description}</h6>
          </div>
        </a>
        <#if video.published>
        <div class="alert alert-success" role="alert">PUBLISHED</div>
        <#else>
        <div class="alert alert-danger" role="alert">NOT PUBLISHED</div>
        </#if>
        <a href="/editVideo/${video.identifier}">edit</a>
      </div>
      </#list>
  </div>
</div>
<#include 'footer.ftl'>
<@footer/>