package com.perjalanan.safarea.data;

public class StockItem {
    private Integer id, productId, userId, qty;
    private Double profitPrice;
    private String name, thumbnail, status, statusDescription;

    public StockItem(
            Integer id,
            Integer productId,
            Integer userId,
            Integer qty,
            String thumbnail,
            Double profitPrice,
            String name,
            String status,
            String statusDescription
    ) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.qty = qty;
        this.thumbnail = thumbnail;
        this.profitPrice = profitPrice;
        this.name = name;
        this.status = status;
        this.statusDescription = statusDescription;
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

    public String getThumbnail() {
        return thumbnail;
    }

    public Double getProfitPrice() {
        return profitPrice;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }
}
