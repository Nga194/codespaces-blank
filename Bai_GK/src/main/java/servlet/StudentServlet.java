// Servlet xử lý đăng nhập và đăng ký tài khoản sinh viên.
package servlet;
 
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
 
import java.io.IOException;
import java.sql.*;
import java.util.regex.Pattern;
 
@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
 
    private static final int MAX_LAN_SAI = 5;
    private static final int PHUT_KHOA = 3;
    private static final Pattern TAIKHOAN_HOP_LE = Pattern.compile("^[a-zA-Z0-9_]{3,50}$");
 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
 
        String action = req.getParameter("action");
 
        if ("register".equals(action)) {
            xuLyDangKy(req, resp);
        } else {
            xuLyDangNhap(req, resp);
        }
    }
 
    // ================== ĐĂNG NHẬP ==================
    private void xuLyDangNhap(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
 
        String taikhoan = req.getParameter("taikhoan");
        String matkhau = req.getParameter("matkhau");
 
        if (taikhoan == null || !TAIKHOAN_HOP_LE.matcher(taikhoan).matches()) {
            req.setAttribute("loi", "Sai tài khoản hoặc mật khẩu!");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
            return;
        }
 
        String sql = "SELECT id, hoten, taikhoan, email, matkhau, so_lan_that_bai, "
                + "thoi_gian_khoa, da_tung_bi_khoa, trang_thai FROM taikhoan WHERE taikhoan = ?";
 
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
 
            ps.setString(1, taikhoan);
 
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    req.setAttribute("loi", "Sai tài khoản hoặc mật khẩu!");
                    req.getRequestDispatcher("index.jsp").forward(req, resp);
                    return;
                }
 
                int id = rs.getInt("id");
                String trangThai = rs.getString("trang_thai");
                Timestamp thoiGianKhoa = rs.getTimestamp("thoi_gian_khoa");
                boolean daTungBiKhoa = rs.getBoolean("da_tung_bi_khoa");
                int soLanSai = rs.getInt("so_lan_that_bai");
                long now = System.currentTimeMillis();

                if ("disabled".equals(trangThai)) {
                    req.setAttribute("loi", "Tài khoản đã bị vô hiệu hóa do đăng nhập sai quá nhiều lần. Vui lòng liên hệ quản trị viên.");
                    req.getRequestDispatcher("index.jsp").forward(req, resp);
                    return;
                }
 
                // 2. Tài khoản đang trong thời gian khóa tạm
                boolean dangBiKhoa = thoiGianKhoa != null && now < thoiGianKhoa.getTime();
                if (dangBiKhoa) {
                    long conLaiGiay = (thoiGianKhoa.getTime() - now) / 1000;
                    long phut = conLaiGiay / 60;
                    long giay = conLaiGiay % 60;
                    req.setAttribute("loi", String.format(
                            "Tài khoản đang tạm khóa do đăng nhập sai nhiều lần. Vui lòng thử lại sau %d phút %d giây.",
                            phut, giay));
                    req.getRequestDispatcher("index.jsp").forward(req, resp);
                    return;
                }
 
                boolean lockVuaHetHan = thoiGianKhoa != null && now >= thoiGianKhoa.getTime();
                if (lockVuaHetHan) {
                    soLanSai = 0;
                }
 
                // 4. Kiểm tra mật khẩu
                String matkhauHash = DBUtil.hashPassword(matkhau);
                if (matkhauHash.equals(rs.getString("matkhau"))) {
                    resetBoDem(conn, id);
 
                    HttpSession session = req.getSession(true);
                    session.setAttribute("id", id);
                    session.setAttribute("hoten", rs.getString("hoten"));
                    session.setAttribute("taikhoan", rs.getString("taikhoan"));
                    session.setAttribute("email", rs.getString("email"));
                    session.setMaxInactiveInterval(30 * 60); // 30 phút
 
                    req.getRequestDispatcher("/ProductServlet").forward(req, resp);
                    return;
                }
 
                soLanSai++;
 
                if (soLanSai >= MAX_LAN_SAI) {
                    if (daTungBiKhoa) {
                        voHieuHoaTaiKhoan(conn, id);
                        req.setAttribute("loi", "Tài khoản đã bị vô hiệu hóa do đăng nhập sai quá nhiều lần. Vui lòng liên hệ quản trị viên.");
                    } else {
                        Timestamp khoaDen = new Timestamp(now + PHUT_KHOA * 60 * 1000L);
                        khoaTamThoi(conn, id, khoaDen);
                        req.setAttribute("loi", String.format(
                                "Bạn đã nhập sai quá %d lần. Tài khoản bị khóa tạm trong %d phút.",
                                MAX_LAN_SAI, PHUT_KHOA));
                    }
                } else {
                    capNhatSoLanSai(conn, id, soLanSai);
                    int conLai = MAX_LAN_SAI - soLanSai;
                    req.setAttribute("loi", String.format(
                            "Sai tài khoản hoặc mật khẩu! Bạn còn %d lần thử trước khi bị khóa.", conLai));
                }
 
                req.getRequestDispatcher("index.jsp").forward(req, resp);
            }
        } catch (SQLException e) {
            throw new ServletException("Lỗi kết nối database", e);
        }
    }
 
    private void resetBoDem(Connection conn, int id) throws SQLException {
        String sql = "UPDATE taikhoan SET so_lan_that_bai = 0, thoi_gian_khoa = NULL WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
 
    private void capNhatSoLanSai(Connection conn, int id, int soLanSai) throws SQLException {
        String sql = "UPDATE taikhoan SET so_lan_that_bai = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, soLanSai);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
 
    private void khoaTamThoi(Connection conn, int id, Timestamp khoaDen) throws SQLException {
        String sql = "UPDATE taikhoan SET so_lan_that_bai = 0, thoi_gian_khoa = ?, da_tung_bi_khoa = 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTimestamp(1, khoaDen);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }
 
    private void voHieuHoaTaiKhoan(Connection conn, int id) throws SQLException {
        String sql = "UPDATE taikhoan SET trang_thai = 'disabled', thoi_gian_khoa = NULL WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
 
    // ================== ĐĂNG KÝ ==================
    private void xuLyDangKy(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
 
        String taikhoan = req.getParameter("taikhoan");
        String email = req.getParameter("email");
        String matkhau = req.getParameter("matkhau");
        String xacnhanmatkhau = req.getParameter("xacnhanmatkhau");

        req.setAttribute("giaTriTaiKhoan", taikhoan);
        req.setAttribute("giaTriEmail", email);
 
        boolean coLoi = false;
 
        if (taikhoan == null || !TAIKHOAN_HOP_LE.matcher(taikhoan).matches()) {
            req.setAttribute("loiTaiKhoan", "Tên tài khoản chỉ được chứa chữ, số, dấu gạch dưới (3-50 ký tự)");
            coLoi = true;
        }
 
        // 2. Kiểm tra email đuôi @gmail.com
        if (email == null || !email.toLowerCase().matches("^[a-zA-Z0-9._%+-]+@gmail\\.com$")) {
            req.setAttribute("loiEmail", "Email phải có đuôi @gmail.com");
            coLoi = true;
        }
 
        // 3. Kiểm tra độ dài mật khẩu tối thiểu 8 ký tự
        if (matkhau == null || matkhau.length() < 8) {
            req.setAttribute("loiMatKhau", "Mật khẩu phải có ít nhất 8 ký tự");
            coLoi = true;
        }
 
        // 4. Kiểm tra xác nhận mật khẩu khớp
        if (matkhau != null && !matkhau.equals(xacnhanmatkhau)) {
            req.setAttribute("loiXacNhan", "Mật khẩu xác nhận không khớp");
            coLoi = true;
        }
 
        // Nếu đã có lỗi từ các bước trên, dừng lại, không cần kiểm tra CSDL
        if (coLoi) {
            req.getRequestDispatcher("Dky.jsp").forward(req, resp);
            return;
        }
 
        // 5. Kiểm tra username đã tồn tại trong CSDL chưa
        String checkSql = "SELECT COUNT(*) FROM taikhoan WHERE taikhoan = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
 
            checkPs.setString(1, taikhoan);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    req.setAttribute("loiTaiKhoan", "Tên tài khoản đã tồn tại, vui lòng chọn tên khác");
                    req.getRequestDispatcher("Dky.jsp").forward(req, resp);
                    return;
                }
            }
 
            // 6. Nếu qua hết các bước kiểm tra -> tiến hành đăng ký
            String insertSql = "INSERT INTO taikhoan (hoten, taikhoan, matkhau, email) VALUES (?, ?, ?, ?)";
            try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                insertPs.setString(1, taikhoan); // tạm dùng username làm họ tên
                insertPs.setString(2, taikhoan);
                insertPs.setString(3, DBUtil.hashPassword(matkhau));
                insertPs.setString(4, email);
                insertPs.executeUpdate();
            }
 
            req.setAttribute("thongbao", "Đăng ký thành công! Vui lòng đăng nhập.");
            req.getRequestDispatcher("index.jsp").forward(req, resp);
 
        } catch (SQLException e) {
            req.setAttribute("loi", "Có lỗi xảy ra, vui lòng thử lại sau");
            req.getRequestDispatcher("Dky.jsp").forward(req, resp);
        }
    }
}