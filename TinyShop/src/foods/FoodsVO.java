package foods;

public class FoodsVO {
    private int foodId; // food_id -> foodId
    private String foodCategory; // food_category -> foodCategory
    private String foodName; // food_name -> foodName
    private String foodDescription; // food_description -> foodDescription
    private long foodPrice; // food_price -> foodPrice

    public FoodsVO(int foodId, String foodCategory, String foodName, String foodDescription, long foodPrice) {
        this.foodId = foodId;
        this.foodCategory = foodCategory;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
    }

    public FoodsVO(String foodCategory, String foodName, String foodDescription, long foodPrice) {
        this.foodCategory = foodCategory;
        this.foodName = foodName;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
    }

    // getter, setter
    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public long getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(long foodPrice) {
        this.foodPrice = foodPrice;
    }

    @Override
    public String toString() {
        return "음식 번호: " + foodId + ", 음식 카테고리: " + foodCategory + ", 음식이름: " + foodName + ", 음식설명: " + foodDescription + ", 가격: " + foodPrice;
    }
}