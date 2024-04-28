// OrdersVO.java
package orders;

public class OrdersVO {
    private int orderId;
    private String foodName;
    private String cusid;
    private int quantity;

    public OrdersVO() {
    }

    public OrdersVO( String foodName, String cusid, int quantity) {
        this.foodName = foodName;
        this.cusid = cusid;
        this.quantity = quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "OrdersVO [orderId=" + orderId  + ", foodName=" + foodName
                + ", cusid=" + cusid + ", quantity=" + quantity + "]";
    }
}