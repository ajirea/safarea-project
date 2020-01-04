package com.perjalanan.safarea.data;

public class StockItem {
    private Integer id, productId, userId, qty, thumbnail;
    private Float profitPrice;
    private String name, status;

    public StockItem(
            Integer id,
            Integer productId,
            Integer userId,
            Integer qty,
            Integer thumbnail,
            Float profitPrice,
            String name,
            String status
    ) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.qty = qty;
        this.thumbnail = thumbnail;
        this.profitPrice = profitPrice;
        this.name = name;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public Integer getProductId() {
        return productId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getQty() {
        return qty;
    }

    public Integer getThumbnail() {
        return thumbnail;
    }

    public Float getProfitPrice() {
        return profitPrice;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }
}
