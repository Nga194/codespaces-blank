<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="servlet.CartItem" %>
<%
    if (session.getAttribute("taikhoan") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    List<CartItem> gioHang = (List<CartItem>) request.getAttribute("gioHang");
    double tongTien = 0;
    if (gioHang != null) {
        for (CartItem item : gioHang) {
            tongTien += item.getThanhTien();
        }
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Giỏ hàng</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="topbar">
    <div class="account-info">
        Xin chào, <b><%= session.getAttribute("hoten") %></b>
    </div>
    <a href="LogoutServlet">Đăng xuất</a>
</div>

<div class="container">
    <h2>Giỏ hàng</h2>

    <div class="nav-row">
        <a href="ProductServlet">← Về danh sách sản phẩm</a>
    </div>

    <% if (gioHang != null && !gioHang.isEmpty()) { %>
        <table class="cart-table">
            <thead>
                <tr>
                    <th>Sản phẩm</th>
                    <th>Đơn giá</th>
                    <th>Số lượng</th>
                    <th>Thành tiền</th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
            <% for (CartItem item : gioHang) { %>
                <tr>
                    <td><%= item.getTenSanPham() %></td>
                    <td><%= String.format("%,.2f", item.getDonGia()) %> USD</td>
                    <td>
                        <form action="CartServlet" method="get" class="qty-form">
                            <input type="hidden" name="action" value="capnhat">
                            <input type="hidden" name="id" value="<%= item.getProductId() %>">
                            <input type="number" name="soluong" min="1" value="<%= item.getSoLuong() %>">
                            <button type="submit" class="btn-sm">Cập nhật</button>
                        </form>
                    </td>
                    <td><%= String.format("%,.2f", item.getThanhTien()) %> USD</td>
                    <td><a href="CartServlet?action=xoa&id=<%= item.getProductId() %>" class="link-danger">Xóa</a></td>
                </tr>
            <% } %>
            </tbody>
        </table>

        <div class="cart-total">Tổng cộng: <b><%= String.format("%,.2f", tongTien) %> USD</b></div>
    <% } else { %>
        <p>Giỏ hàng đang trống.</p>
    <% } %>
</div>

</body>
</html>
