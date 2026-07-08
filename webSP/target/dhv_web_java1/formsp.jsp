<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="servlet.ProductServlet.Category" %>
<%@ page import="servlet.ProductServlet.Product" %>
<%
    if (session.getAttribute("taikhoan") == null) {
        response.sendRedirect("index.jsp");
        return;
    }
    Product sp = (Product) request.getAttribute("sanPham");
    boolean laSua = (sp != null);
    List<Category> danhMuc = (List<Category>) request.getAttribute("danhSachDanhMuc");
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title><%= laSua ? "Sửa sản phẩm" : "Thêm sản phẩm" %></title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
<div class="container">
    <h2><%= laSua ? "Sửa sản phẩm" : "Thêm sản phẩm mới" %></h2>

    <form action="ProductServlet" method="post">
        <input type="hidden" name="action" value="<%= laSua ? "Cap Nhat" : "add" %>">
        <% if (laSua) { %>
            <input type="hidden" name="productID" value="<%= sp.getId() %>">
        <% } %>

        <div class="form-group">
            <label>Tên sản phẩm</label>
            <input type="text" name="productName" required
                   value="<%= laSua ? sp.getTenSp() : "" %>">
        </div>

        <div class="form-group">
            <label>Đơn giá</label>
            <input type="number" step="0.01" name="unitPrice" required
                   value="<%= laSua ? sp.getGia() : "" %>">
        </div>

        <div class="form-group">
            <label>Quy cách đóng gói</label>
            <input type="text" name="quantityPerUnit"
                   value="<%= laSua && sp.getQuyCach() != null ? sp.getQuyCach() : "" %>">
        </div>

        <div class="form-group">
            <label>Số lượng tồn</label>
            <input type="number" name="unitsInStock" required
                   value="<%= laSua ? sp.getSoLuongTon() : "" %>">
        </div>

        <div class="form-group">
            <label>Danh mục</label>
            <select name="categoryID" required>
                <option value="">-- Chọn danh mục --</option>
                <% if (danhMuc != null) {
                    for (Category dm : danhMuc) { %>
                        <option value="<%= dm.getId() %>"
                            <%= (laSua && sp.getCategoryId() == dm.getId()) ? "selected" : "" %>>
                            <%= dm.getTen() %>
                        </option>
                <%  }
                } %>
            </select>
        </div>

        <button type="submit" class="btn"><%= laSua ? "Cập nhật" : "Thêm" %></button>
        <a href="ProductServlet" class="btn">Hủy</a>
    </form>
</div>
</body>
</html>
