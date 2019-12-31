package my.id.satria.safarea.data;


import java.util.Date;

public class TransactionItem {

    private int image, id, userid, qty;
    private String name, product, phone, orderedAt, orderDesc;
    private double price, totalPrice;


    public TransactionItem(Integer image, Integer id, Integer userid, double price, double totalPrice, Integer qty,
                           String product, String name, String phone, String orderDesc)
    {
        this.id = id;
        this.userid = userid;
        this.price = price;
        this.totalPrice = totalPrice;
        this.qty = qty;
        this.name = name;
        this.phone = phone;
        this.orderDesc = orderDesc;
        this.product = product;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(String orderDesc) {
        this.orderDesc = orderDesc;
    }

    public String getOrderedAt() {
        return orderedAt;
    }

    public void setOrderedAt(String orderedAt) {
        this.orderedAt = orderedAt;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }
}
