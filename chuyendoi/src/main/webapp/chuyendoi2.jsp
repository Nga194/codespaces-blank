<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>KM to Miles</title>
</head>
<body>

<h2>Unit Convert</h2>
 <h1>Chuyển đổi giá trị</h1>

<form action="convert2" method="post">
   

    Miles:
    <input type="text" name="km">

    <button type="submit">Convert</button>
</form>

     <%
    Object result = request.getAttribute("result");

    if(result != null){
    %>

        <h3>Result: <%= result %> miles</h3>

    
    <%
        }
    %>



</body>
</html>