<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng nhập</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-box">
        <h2>Đăng nhập</h2>

        <c:if test="${not empty loi}">
            <div class="error-msg"><c:out value="${loi}"/></div>
        </c:if>
        <c:if test="${not empty thongbao}">
            <div class="success-msg"><c:out value="${thongbao}"/></div>
        </c:if>

       <form action="StudentServlet" method="post">
            <input type="hidden" name="action" value="login">
            <div class="form-group">
                <label>Tài khoản</label>
                <input type="text" name="taikhoan" required>
            </div>
            <div class="form-group">
                <label>Mật khẩu</label>
                <input type="password" name="matkhau" required>
            </div>
            <button type="submit" class="btn">Đăng nhập</button>
        </form>

        <div class="link-row">
            Chưa có tài khoản? <a href="Dky.jsp">Đăng ký ngay</a>
        </div>
    </div>
</div>
</body>
</html>
