package my.id.satria.safarea.data;

public class CatalogItem {

    private Integer id, thumbnail;
    private String title;
    private Float price;

    public CatalogItem(Integer id, Integer thumbnail, String title, Float price) {
        this.id = id;
        this.thumbnail = thumbnail;
        this.title = title;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public Integer getThumbnail() {
        return thumbnail;
    }

    public String getTitle() {
        return title;
    }

    public Float getPrice() {
        return price;
    }
}
