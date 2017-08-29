<%
  response.setContentType("text/html;charset=UTF-8");
  out.print(session.getAttribute("echo"));
%>