<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <title>Kết quả chuyển đổi</title>
    <h1>ĐÂY LÀ TRANG RESULT.JSP</h1>
</head>
<body>
    
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