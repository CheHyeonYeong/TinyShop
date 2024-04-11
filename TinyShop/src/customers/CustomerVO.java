package customers;

public class CustomerVO {
    private String id;
    private String pw;
    private String name;
    //기본생성자
    public CustomerVO () {}
    //필드생성자
    public CustomerVO(String id, String pw, String name) {
        this.id = id;
        this.pw = pw;
        this.name = name;
    }
    @Override
    public String toString() {
        return "CustomerVo[id=" +id+ ", pw=" +pw+", name = " +name+ "]";
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getPw() {
        return pw;
    }
    public void setPw(String pw) {
        this.pw = pw;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
