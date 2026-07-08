package servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        if (!daDangNhap(req)) {
            req.getRequestDispatcher("index.jsp").forward(req, resp);
            return;
        }

        String taikhoan = (String) req.getSession().getAttribute("taikhoan");
        String action = req.getParameter("action");
        if (action == null) action = "xem";

        switch (action) {
            case "them":
                themVaoGio(req, taikhoan);
                break;
            case "xoa":
                xoaKhoiGio(req, taikhoan);
                break;
            case "capnhat":
                capNhatSoLuong(req, taikhoan);
                break;
        }

        hienGioHang(req, resp, taikhoan);
    }

    // ================== KIỂM TRA ĐĂNG NHẬP ==================
    private boolean daDangNhap(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session != null && session.getAttribute("taikhoan") != null;
    }

    // ================== THÊM SẢN PHẨM VÀO GIỎ ==================
    private void themVaoGio(HttpServletRequest req, String taikhoan) throws ServletException {
        int id = parseIntAnToan(req.getParameter("id"));

        String sql = "INSERT INTO GioHangChiTiet (taikhoan, productId, soLuong) VALUES (?, ?, 1) "
                   + "ON DUPLICATE KEY UPDATE soLuong = soLuong + 1";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, taikhoan);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new ServletException("Lỗi khi thêm vào giỏ hàng", e);
        }
    }

    // ================== XÓA 1 DÒNG KHỎI GIỎ ==================
    private void xoaKhoiGio(HttpServletRequest req, String taikhoan) throws ServletException {
        int id = parseIntAnToan(req.getParameter("id"));

        String sql = "DELETE FROM GioHangChiTiet WHERE taikhoan = ? AND productId = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, taikhoan);
            ps.setInt(2, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new ServletException("Lỗi khi xóa khỏi giỏ hàng", e);
        }
    }

    // ================== CẬP NHẬT SỐ LƯỢNG ==================
    private void capNhatSoLuong(HttpServletRequest req, String taikhoan) throws ServletException {
        int id = parseIntAnToan(req.getParameter("id"));
        int soLuong = parseIntAnToan(req.getParameter("soluong"));
        if (soLuong < 1) soLuong = 1;

        String sql = "UPDATE GioHangChiTiet SET soLuong = ? WHERE taikhoan = ? AND productId = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, soLuong);
            ps.setString(2, taikhoan);
            ps.setInt(3, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            throw new ServletException("Lỗi khi cập nhật số lượng", e);
        }
    }

    // ================== HIỂN THỊ GIỎ HÀNG ==================
    private void hienGioHang(HttpServletRequest req, HttpServletResponse resp, String taikhoan)
            throws ServletException, IOException {

        List<CartItem> gioHang = layGioHangTuDb(taikhoan);
        req.setAttribute("gioHang", gioHang);
        req.getRequestDispatcher("giohang.jsp").forward(req, resp);
    }

    private List<CartItem> layGioHangTuDb(String taikhoan) throws ServletException {
        List<CartItem> ds = new ArrayList<>();

        String sql = "SELECT g.productId, p.productName, p.unitPrice, g.soLuong "
                   + "FROM GioHangChiTiet g "
                   + "JOIN Product p ON g.productId = p.productID "
                   + "WHERE g.taikhoan = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, taikhoan);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(new CartItem(
                            rs.getInt("productId"),
                            rs.getString("productName"),
                            rs.getDouble("unitPrice"),
                            rs.getInt("soLuong")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Lỗi khi lấy giỏ hàng", e);
        }

        return ds;
    }

    // ================== ĐẾM TỔNG SỐ LƯỢNG (dùng cho icon giỏ hàng) ==================
    public static int demSoLuong(String taikhoan) {
        if (taikhoan == null) return 0;
        int tong = 0;
        String sql = "SELECT COALESCE(SUM(soLuong), 0) AS tong FROM GioHangChiTiet WHERE taikhoan = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, taikhoan);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) tong = rs.getInt("tong");
            }
        } catch (SQLException e) {
        }
        return tong;
    }

    private int parseIntAnToan(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
}
