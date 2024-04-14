package orders;
//
public class OrdersVO {

    //- 주문 생성: 주문 생성 시 고객과 해당 음식의 정보 (이름, 수량 포함)
    //고객 ID
    private int id;
    //음식 이름
    private String foodName;
    //음식의 수량
    private int quantity;

    //기본생성자
    public OrdersVO(){}

    //필드생성자
    public OrdersVO(int id, String foodName, int quantity) {
        this.id = id;
        this.foodName = foodName;
        this.quantity = quantity;
    }


    //getter , setter
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getFoodName() {
        return foodName;
    }
    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

}