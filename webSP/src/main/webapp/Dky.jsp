<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Đăng ký</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="auth-wrapper">
    <div class="auth-box">
        <h2>Đăng ký tài khoản</h2>

        <form action="StudentServlet" method="post" onsubmit="return kiemTra()" novalidate>
            <input type="hidden" name="action" value="register">
            <div class="form-group">
                <label>Username</label>
                <input type="text" id="taikhoan" name="taikhoan"
                       value="<%= request.getAttribute("giaTriTaiKhoan") != null ? request.getAttribute("giaTriTaiKhoan") : "" %>">
                <div class="field-error" id="loi-taikhoan"><%= request.getAttribute("loiTaiKhoan") != null ? request.getAttribute("loiTaiKhoan") : "" %></div>
            </div>
            <div class="form-group">
                <label>Email</label>
                <input type="text" id="email" name="email"
                       value="<%= request.getAttribute("giaTriEmail") != null ? request.getAttribute("giaTriEmail") : "" %>">
                <div class="field-error" id="loi-email"><%= request.getAttribute("loiEmail") != null ? request.getAttribute("loiEmail") : "" %></div>
            </div>
            <div class="form-group">
                <label>Password</label>
                <input type="password" id="matkhau" name="matkhau">
                <div class="field-error" id="loi-matkhau"><%= request.getAttribute("loiMatKhau") != null ? request.getAttribute("loiMatKhau") : "" %></div>
            </div>
            <div class="form-group">
                <label>Confirm Password</label>
                <input type="password" id="xacnhanmatkhau" name="xacnhanmatkhau">
                <div class="field-error" id="loi-xacnhanmatkhau"><%= request.getAttribute("loiXacNhan") != null ? request.getAttribute("loiXacNhan") : "" %></div>
            </div>
            <button type="submit" class="btn">Đăng ký</button>
        </form>

        <div class="link-row">
            Đã có tài khoản? <a href="index.jsp">Đăng nhập</a>
        </div>
    </div>
</div>

<script>
function xoaLoiCu() {
    document.querySelectorAll('.field-error').forEach(el => el.textContent = '');
}

function hienLoi(id, thongBao) {
    document.getElementById(id).textContent = thongBao;
}

function kiemTra() {
    xoaLoiCu();
    let hopLe = true;

    const taikhoan = document.getElementById('taikhoan').value.trim();
    const email = document.getElementById('email').value.trim();
    const mk = document.getElementById('matkhau').value;
    const xnmk = document.getElementById('xacnhanmatkhau').value;

    if (taikhoan === '') {
        hienLoi('loi-taikhoan', 'Vui lòng nhập tên tài khoản');
        hopLe = false;
    }

    if (email === '') {
        hienLoi('loi-email', 'Vui lòng nhập email');
        hopLe = false;
    } else if (!email.toLowerCase().endsWith('@gmail.com')) {
        hienLoi('loi-email', 'Email phải có đuôi @gmail.com');
        hopLe = false;
    }

    if (mk === '') {
        hienLoi('loi-matkhau', 'Vui lòng nhập mật khẩu');
        hopLe = false;
    } else if (mk.length < 8) {
        hienLoi('loi-matkhau', 'Mật khẩu phải có ít nhất 8 ký tự');
        hopLe = false;
    }

    if (xnmk === '') {
        hienLoi('loi-xacnhanmatkhau', 'Vui lòng xác nhận mật khẩu');
        hopLe = false;
    } else if (mk !== xnmk) {
        hienLoi('loi-xacnhanmatkhau', 'Mật khẩu xác nhận không khớp');
        hopLe = false;
    }

    return hopLe;
}
</script>
</body>
</html>
