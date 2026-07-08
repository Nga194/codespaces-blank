package servlet;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
 
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
 
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
 
        if (!daDangNhap(req)) {
            req.getRequestDispatcher("index.jsp").forward(req, resp);
            return;
        }
 
        String action = req.getParameter("action");
        if (action == null) action = "list";
 
        switch (action) {
            case "new":
                hienFormThem(req, resp);
                break;
            case "edit":
                hienFormSua(req, resp);
                break;
            case "Xoa":
                xoaSanPham(req, resp);
                hienDanhSach(req, resp, "dangban");
                break;
            case "tamhet":
                hienDanhSach(req, resp, "tamhet");
                break;
            case "ngung":
                hienDanhSach(req, resp, "ngung");
                break;
            case "khoiphuc":
                khoiPhucSanPham(req, resp);
                hienDanhSach(req, resp, "dangban");
                break;
            default:
                hienDanhSach(req, resp, "dangban");
        }
    }
 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
 
        if (!daDangNhap(req)) {
            req.getRequestDispatcher("index.jsp").forward(req, resp);
            return;
        }
 
        String action = req.getParameter("action");
 
        if ("add".equals(action)) {
            themSanPham(req, resp);
        } else if ("Cap Nhat".equals(action)) {
            capNhatSanPham(req, resp);
        }
 
        hienDanhSach(req, resp, "dangban");
    }
 
    // ================== KIỂM TRA ĐĂNG NHẬP ==================
    private boolean daDangNhap(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute("taikhoan") != null;
    }
 
    // ================== HIỂN THỊ DANH SÁCH THEO NHÓM ==================
    // loai: "dangban" | "tamhet" | "ngung"
    private void hienDanhSach(HttpServletRequest req, HttpServletResponse resp, String loai)
            throws ServletException, IOException {
 
        List<Product> danhSach = layDanhSachTheoLoai(loai);
        req.setAttribute("danhSachSanPham", danhSach);
        req.setAttribute("nhomHienTai", loai);
        req.getRequestDispatcher("sanpham.jsp").forward(req, resp);
    }
 
    private List<Product> layDanhSachTheoLoai(String loai) throws ServletException {
        List<Product> ds = new ArrayList<>();
        int discontinued = "ngung".equals(loai) ? 1 : 0;
 
        String sql = "SELECT p.productID, p.productName, p.unitPrice, p.quantityPerUnit, "
                   + "p.unitsInStock, p.categoryID, c.categoryName, "
                   + "(COALESCE(p.unitsInStock, 0) - COALESCE(da.soLuongDaDat, 0)) AS soLuongConLai "
                   + "FROM Product p "
                   + "LEFT JOIN Category c ON p.categoryID = c.categoryID "
                   + "LEFT JOIN ("
                   + "    SELECT od.productId, SUM(od.quantity) AS soLuongDaDat "
                   + "    FROM OrderDetail od "
                   + "    JOIN SalesOrder so ON od.orderId = so.orderId "
                   + "    WHERE so.shippedDate IS NULL "
                   + "    GROUP BY od.productId"
                   + ") da ON da.productId = p.productID "
                   + "WHERE p.discontinued = ? ";
 
        if ("tamhet".equals(loai)) {
            sql += "HAVING soLuongConLai <= 0";
        } else if ("dangban".equals(loai)) {
            sql += "HAVING soLuongConLai > 0";
        }
 
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, discontinued);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new Product(
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getDouble("unitPrice"),
                            rs.getString("quantityPerUnit"),
                            rs.getInt("unitsInStock"),
                            rs.getInt("categoryID"),
                            rs.getString("categoryName"),
                            rs.getInt("soLuongConLai")
                    ));
                }
            }
 
        } catch (SQLException e) {
            throw new ServletException("Lỗi kết nối database", e);
        }
 
        return ds;
    }
 
    private void hienFormThem(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
 
        req.setAttribute("danhSachDanhMuc", layDanhMuc());
        req.getRequestDispatcher("formsp.jsp").forward(req, resp);
    }

    private void hienFormSua(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
 
        int id = Integer.parseInt(req.getParameter("id"));
        String sql = "SELECT productID, productName, unitPrice, quantityPerUnit, "
                   + "unitsInStock, categoryID FROM Product WHERE productID = ?";
 
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product sp = new Product(
                            rs.getInt("productID"),
                            rs.getString("productName"),
                            rs.getDouble("unitPrice"),
                            rs.getString("quantityPerUnit"),
                            rs.getInt("unitsInStock"),
                            rs.getInt("categoryID"),
                            null,
                            0
                    );
                    req.setAttribute("sanPham", sp);
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Lỗi kết nối database", e);
        }
 
        req.setAttribute("danhSachDanhMuc", layDanhMuc());
        req.getRequestDispatcher("formsp.jsp").forward(req, resp);
    }
 
    // ================== THÊM SẢN PHẨM ==================
    private void themSanPham(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
 
        String ten = req.getParameter("productName");
        double gia = parseDoubleAnToan(req.getParameter("unitPrice"));
        String quyCach = req.getParameter("quantityPerUnit");
        int tonKho = parseIntAnToan(req.getParameter("unitsInStock"));
        int danhMuc = parseIntAnToan(req.getParameter("categoryID"));
 
        String sql = "INSERT INTO Product (productName, unitPrice, quantityPerUnit, unitsInStock, categoryID, discontinued) "
                   + "VALUES (?, ?, ?, ?, ?, 0)";
 
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, ten);
            ps.setDouble(2, gia);
            ps.setString(3, quyCach);
            ps.setInt(4, tonKho);
            ps.setInt(5, danhMuc);
            ps.executeUpdate();
 
        } catch (SQLException e) {
            throw new ServletException("Lỗi khi thêm sản phẩm", e);
        }
    }
 
    // ================== CẬP NHẬT SẢN PHẨM ==================
    private void capNhatSanPham(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
 
        int id = parseIntAnToan(req.getParameter("productID"));
        String ten = req.getParameter("productName");
        double gia = parseDoubleAnToan(req.getParameter("unitPrice"));
        String quyCach = req.getParameter("quantityPerUnit");
        int tonKho = parseIntAnToan(req.getParameter("unitsInStock"));
        int danhMuc = parseIntAnToan(req.getParameter("categoryID"));
 
        String sql = "UPDATE Product SET productName = ?, unitPrice = ?, quantityPerUnit = ?, "
                   + "unitsInStock = ?, categoryID = ? WHERE productID = ?";
 
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, ten);
            ps.setDouble(2, gia);
            ps.setString(3, quyCach);
            ps.setInt(4, tonKho);
            ps.setInt(5, danhMuc);
            ps.setInt(6, id);
            ps.executeUpdate();
 
        } catch (SQLException e) {
            throw new ServletException("Lỗi khi cập nhật sản phẩm", e);
        }
    }
 
    // ================== XÓA SẢN PHẨM ==================
    private void xoaSanPham(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
 
        int id = parseIntAnToan(req.getParameter("id"));
        String sql = "DELETE FROM Product WHERE productID = ?";
 
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, id);
            ps.executeUpdate();
 
        } catch (SQLIntegrityConstraintViolationException e) {
            capNhatTrangThaiNgung(id, 1);
        } catch (SQLException e) {
            throw new ServletException("Lỗi khi xóa sản phẩm", e);
        }
    }
 
    // ================== KHÔI PHỤC SẢN PHẨM NGỪNG KINH DOANH ==================
    private void khoiPhucSanPham(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
 
        int id = parseIntAnToan(req.getParameter("id"));
        capNhatTrangThaiNgung(id, 0);
    }
 
    private void capNhatTrangThaiNgung(int id, int trangThai) throws ServletException {
        String sql = "UPDATE Product SET discontinued = ? WHERE productID = ?";
 
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setInt(1, trangThai);
            ps.setInt(2, id);
            ps.executeUpdate();
 
        } catch (SQLException e) {
            throw new ServletException("Lỗi khi cập nhật trạng thái sản phẩm", e);
        }
    }
 
    // ================== LẤY DANH MỤC ==================
    private List<Category> layDanhMuc() throws ServletException {
        List<Category> ds = new ArrayList<>();
        String sql = "SELECT categoryID, categoryName FROM Category ORDER BY categoryName";
 
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
 
            while (rs.next()) {
                ds.add(new Category(rs.getInt("categoryID"), rs.getString("categoryName")));
            }
        } catch (SQLException e) {
            throw new ServletException("Lỗi khi lấy danh mục", e);
        }
        return ds;
    }
 
    private int parseIntAnToan(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
 
    private double parseDoubleAnToan(String s) {
        try { return Double.parseDouble(s); } catch (Exception e) { return 0; }
    }
 
    public static class Product {
        private int id;
        private String tenSp;
        private double gia;
        private String quyCach;
        private int soLuongTon;
        private int categoryId;
        private String danhMuc;
        private int soLuongConLai;
 
        public Product(int id, String tenSp, double gia, String quyCach, int soLuongTon,
                       int categoryId, String danhMuc, int soLuongConLai) {
            this.id = id;
            this.tenSp = tenSp;
            this.gia = gia;
            this.quyCach = quyCach;
            this.soLuongTon = soLuongTon;
            this.categoryId = categoryId;
            this.danhMuc = danhMuc;
            this.soLuongConLai = soLuongConLai;
        }
 
        public int getId() { return id; }
        public String getTenSp() { return tenSp; }
        public double getGia() { return gia; }
        public String getQuyCach() { return quyCach; }
        public int getSoLuongTon() { return soLuongTon; }
        public int getCategoryId() { return categoryId; }
        public String getDanhMuc() { return danhMuc; }
        public int getSoLuongConLai() { return soLuongConLai; }
    }
 
    public static class Category {
        private int id;
        private String ten;
 
        public Category(int id, String ten) {
            this.id = id;
            this.ten = ten;
        }
 
        public int getId() { return id; }
        public String getTen() { return ten; }
    }
}
 