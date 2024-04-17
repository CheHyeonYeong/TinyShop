package foods;

public class FoodsVO {
    private int food_id;
    private String food_category;
    private String food_name;
    private String food_description;
    private long food_price;

    public FoodsVO(int food_id, String food_category, String food_name, String food_description, long food_price) {
        this.food_id = food_id;
        this.food_category = food_category;
        this.food_name = food_name;
        this.food_description = food_description;
        this.food_price = food_price;
    }

    public FoodsVO(String food_category, String food_name, String food_description, long food_price) {
        this.food_category = food_category;
        this.food_name = food_name;
        this.food_description = food_description;
        this.food_price = food_price;
    }

    // getter, setter
    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFood_category() {
        return food_category;
    }

    public void setFood_category(String food_category) {
        this.food_category = food_category;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_description() {
        return food_description;
    }

    public void setFood_description(String food_description) {
        this.food_description = food_description;
    }

    public long getFood_price() {
        return food_price;
    }

    public void setFood_price(long food_price) {
        this.food_price = food_price;
    }

    @Override
    public String toString() {
        return "음식 번호: " + food_id + ", 음식 카테고리: " + food_category + ", 음식이름: " + food_name + ", 음식설명: " + food_description + ", 가격: " + food_price;
    }
}