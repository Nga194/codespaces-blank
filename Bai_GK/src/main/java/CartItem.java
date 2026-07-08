package servlet;
 
/**
 * Đại diện 1 dòng sản phẩm trong giỏ hàng (lưu trong session).
 */
public class CartItem {
    private int productId;
    private String tenSanPham;
    private double donGia;
    private int soLuong;
 
    public CartItem(int productId, String tenSanPham, double donGia, int soLuong) {
        this.productId = productId;
        this.tenSanPham = tenSanPham;
        this.donGia = donGia;
        this.soLuong = soLuong;
    }
 
    public int getProductId() { return productId; }
    public String getTenSanPham() { return tenSanPham; }
    public double getDonGia() { return donGia; }
    public int getSoLuong() { return soLuong; }
    public void setSoLuong(int soLuong) { this.soLuong = soLuong; }
 
    public double getThanhTien() {
        return donGia * soLuong;
    }
}
 