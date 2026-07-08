<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="servlet.ProductServlet.Product" %>
<%@ page import="servlet.CartItem" %>
<%@ page import="servlet.CartServlet" %>
<%
    if (session.getAttribute("taikhoan") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    List<Product> danhSach = (List<Product>) request.getAttribute("danhSachSanPham");
   int soLuongGioHang = CartServlet.demSoLuong((String) session.getAttribute("taikhoan"));
    String nhom = (String) request.getAttribute("nhomHienTai");
    if (nhom == null) nhom = "dangban";

  String tieuDe;
    if ("tamhet".equals(nhom)) {
        tieuDe = "Sản phẩm tạm hết hàng";
    } else if ("ngung".equals(nhom)) {
        tieuDe = "Sản phẩm ngừng kinh doanh";
    } else {
        tieuDe = "Danh sách sản phẩm (Northwind)";
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Sản phẩm</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>

<div class="topbar">
    <div class="account-info">
        Chào mừng trở lại, <b><%= session.getAttribute("hoten") %></b>! Hãy chọn sản phẩm ưng ý cho giỏ hàng của bạn nhé.
    </div>
     <a href="CartServlet" style="background:#1F4B43; margin-right:8px; display:inline-flex; align-items:center; gap:6px;">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <circle cx="9" cy="21" r="1"></circle>
            <circle cx="20" cy="21" r="1"></circle>
            <path d="M1 1h4l2.68 13.39a2 2 0 0 0 2 1.61h9.72a2 2 0 0 0 2-1.61L23 6H6"></path>
        </svg>
        <%= soLuongGioHang %>
    </a>
    <a href="LogoutServlet">Đăng xuất</a>
</div>

<div class="container">
    <h2><%= tieuDe %></h2>

    <div class="nav-row">
        <a href="ProductServlet" class="<%= "dangban".equals(nhom) ? "active" : "" %>">Đang bán</a> |
        <a href="ProductServlet?action=tamhet" class="<%= "tamhet".equals(nhom) ? "active" : "" %>">Tạm hết hàng</a> |
        <a href="ProductServlet?action=ngung" class="<%= "ngung".equals(nhom) ? "active" : "" %>">Ngừng kinh doanh</a>
        <% if ("dangban".equals(nhom)) { %>
            | <a href="ProductServlet?action=new" class="btn">+ Thêm sản phẩm</a>
        <% } %>
    </div>

    <div class="product-grid">
        <% if (danhSach != null && !danhSach.isEmpty()) {
            for (Product sp : danhSach) { %>
                <div class="product-card">
                    <h3><%= sp.getTenSp() %></h3>
                    <div class="price"><%= String.format("%,.2f", sp.getGia()) %> USD</div>
                    <p>Quy cách: <%= sp.getQuyCach() != null ? sp.getQuyCach() : "-" %></p>
                    <p>Tồn kho: <%= sp.getSoLuongTon() %></p>
                    <p>Danh mục: <%= sp.getDanhMuc() != null ? sp.getDanhMuc() : "-" %></p>
                    <% if ("tamhet".equals(nhom)) { %>
                        <p style="color:#c0392b;">Sau khi giao hết đơn còn: <%= sp.getSoLuongConLai() %></p>
                    <% } %>

                    <div class="product-actions">
                        <% if ("ngung".equals(nhom)) { %>
                            <a href="ProductServlet?action=khoiphuc&id=<%= sp.getId() %>">Khôi phục</a>
                        <% } else { %>
                            <a href="ProductServlet?action=edit&id=<%= sp.getId() %>">Sửa</a>
                            <a href="ProductServlet?action=Xoa&id=<%= sp.getId() %>"
                               onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?');">Xóa</a>
                            <% if ("dangban".equals(nhom)) { %>
                                <a href="CartServlet?action=them&id=<%= sp.getId() %>">+ Giỏ hàng</a>
                            <% } %>
                        <% } %>
                    </div>
                </div>
        <%  }
        } else { %>
            <p>Không có sản phẩm nào trong nhóm này.</p>
        <% } %>
    </div>
</div>

</body>
</html>
