package foods;

public class FoodsVO {
    private int id;
    private String category;
    private String name;
    private String description;
    private long price;

    public FoodsVO(int id, String category, String name, String description, long price) {
        this.id = id;
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    public FoodsVO(String category, String name, String description, long price) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    // getter, setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "음식 번호: " + id + ", 음식 카테고리: " + category + ", 음식이름: " + name + ", 음식설명: " + description + ", 가격: " + price;
    }
}
