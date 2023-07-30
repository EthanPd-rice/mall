package com.ethan.mall;


public enum OrderEnum {
    PAID(0,"已支付，未发货"),DELVERED(1,"已支付，已发货"),RECELIED(2,"已收货");

    private int state;
    private String stateInfo;

    OrderEnum(){

    }

    OrderEnum(int state,String stateInfo){
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    @Override
    public String toString() {
        return "orderEnum{" +
                "state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                '}';
    }

    public static void main(String[] args) {
        //输出PAID对应的状态
        System.out.println("PAID的状态"+OrderEnum.PAID);
        //获取DELVERED的状态码和状态信息并输出
        int state = OrderEnum.DELVERED.getState();
        String stateInfo = OrderEnum.DELVERED.getStateInfo();
        System.out.println("DELVERED的状态码："+state+",DELVERED的状态信息："+stateInfo);
        OrderEnum orderEnum = OrderEnum.RECELIED;
        System.out.println("RECELIED的状态信息是："+orderEnum.getStateInfo());
    }
}
