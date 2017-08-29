<!doctype html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Login Form with Email Password Link</title>
    <!--<link rel="stylesheet" media="screen" href="${pageContext.request.contextPath}/resource/css/jquery.validation.css">-->
    <script src="${pageContext.request.contextPath}/resource/flatui/js/vendor/jquery.min.js"></script>
    <script src="${pageContext.request.contextPath}/resource/js/jquery.validate.min.js"></script>
    <script>
      $(function() {
        // highlight
//        var elements = $("input[type!='submit'], textarea, select");
//        elements.focus(function() {
//          $(this).parents('li').addClass('highlight');
//        });
//        elements.blur(function() {
//          $(this).parents('li').removeClass('highlight');
//        });
//
//        $("#forgotpassword").click(function() {
//          $("#password").removeClass("required");
//          $("#login").submit();
//          $("#password").addClass("required");
//          return false;
//        });

        $("#login").validate()
      });
    </script>
  </head>
  <body>
    <div id="page">
      <div id="content">
        <form action="" method="get" id="login">
          <fieldset>
            <legend>User details</legend>
            <ul>
              <li>
                <input id="email" name="email" class="text required email" type="text">
              </li>
              <li>
                <input name="password" type="password" class="text required" id="password" minlength="4" maxlength="20">
              </li>
            </ul>
          </fieldset>
          <fieldset class="submit">
            <input type="submit" class="button" value="Login...">
          </fieldset>
        </form>
      </div>
    </div>
  </body>
</html>
