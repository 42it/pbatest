<#include 'header.ftl'>
<#include 'footer.ftl'>
<@header/>
<form id="uploadForm">
  <input type="file" id="video">
  <input type="submit" value="Upload video">
</form>
<@footer>
<script>
$( "#uploadForm" ).submit(function( event ) {
  uploadFile();
  event.preventDefault();
});

function uploadFile() {
  var fileInput = $("#video")[0];
  for (var file, i = 0; i < fileInput.files.length; i++) {
    file = fileInput.files[i];
    $.ajax({
      url : '/generatePresignedUploadUrl',
      data: 'file='+ file.name + '&mime=' + file.type,
      type : "GET",
      dataType : "text",
      cache : false,
    })
    .success(function(s3presignedUrl) {
      $.ajax({
        url : s3presignedUrl,
        type : "PUT",
        data : file,
        dataType : "text",
        cache : false,
        contentType : file.type,
        processData : false
      })
      .success(function(result){
        $.ajax({
          url : '/uploadSuccessful',
          type : "PUT",
          data : 'file='+ file.name,
          dataType : "text",
          cache : false
        }).success(function(result){
        window.location = '/editVideo/' + result;
        })
      })
      .error(function(){
        console.error('damn...');
      });
    });
  }
};
</script>
</@footer>