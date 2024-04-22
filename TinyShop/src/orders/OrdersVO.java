// OrdersVO.java
package orders;

public class OrdersVO {
    private int orderId;
    private int foodId;
    private String foodName;
    private String cusid;
    private String cusname;
    private int quantity;

    public OrdersVO() {
    }

    public OrdersVO(int foodId, String foodName, String cusid, String cusname, int quantity) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.cusid = cusid;
        this.cusname = cusname;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getCusid() {
        return cusid;
    }

    public void setCusid(String cusid) {
        this.cusid = cusid;
    }

    public String getCusname() {
        return cusname;
    }

    public void setCusname(String cusname) {
        this.cusname = cusname;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrdersVO [orderId=" + orderId + ", foodId=" + foodId + ", foodName=" + foodName + ", cusid=" + cusid
                + ", cusname=" + cusname + ", quantity=" + quantity + "]";
    }
}