<#include 'header.ftl'>
<#include 'footer.ftl'>
<@header/>
<div class="container">
  <div class="content">
      <form name="form" action="/editVideo/${video.identifier}" method="POST">
        <fieldset>
          <h3>${video.filename}</h3>    
          <div class="form-group">
            <label for="description">Description</label>
            <textarea id="description" name="description" class="form-control" rows="2">${video.description!}</textarea>
          </div>
          
          <div class="form-group">
            <label for="published">Published?</label>
            <input type="checkbox" name="published" <#if video.published>checked="true"</#if> />
          </div>
          
        </fieldset>
        <input type="submit" value="Update" class="btn btn-primary" />
      </form>
    </div>
</div>
<@footer/>
