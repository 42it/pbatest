<#include 'header.ftl'>
<#include 'footer.ftl'>
<@header>
  <link href="//vjs.zencdn.net/4.12/video-js.css" rel="stylesheet">
</@header>
<div class="container">
  <video id="example_video_randalf" class="video-js vjs-default-skin"
    controls preload="auto" autoplay width="640" height="360"
    poster="/images/randalf_poster.webp">
   <source src="${videoUrl}" type='video/mp4' />
   <p class="vjs-no-js">To view this video please enable JavaScript, and consider upgrading to a web browser that <a href="http://videojs.com/html5-video-support/" target="_blank">supports HTML5 video</a></p>
  </video>
</div>
<@footer>
  <script src="//vjs.zencdn.net/4.12/video.js"></script>
  <script type="text/javascript">
    document.createElement('video');document.createElement('audio');document.createElement('track');
  </script>
</@footer>