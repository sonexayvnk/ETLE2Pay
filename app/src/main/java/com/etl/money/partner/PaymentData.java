package com.etl.money.partner;

public class PaymentData  {
    private int Ids;
    private int IdType;
    private String Title;
    private String Description;
    private String CirclesTitle;
    private String CirclesColor;

    public PaymentData(int IdType, String Title, String Description, String CirclesTitle, String CirclesColor) {

        this.IdType = IdType;
        this.Title = Title;
        this.Description = Description;
        this.CirclesTitle = CirclesTitle;
        this.CirclesColor = CirclesColor;
    }

    public int getIdType() {
        return IdType;
    }
    public String getTitle() { return Title; }
    public String getDescription () {
        return Description;
    }
    public String getCirclesTitle() {
        return CirclesTitle;
    }
    public String getCirclesColor() {
        return CirclesColor;
    }


}