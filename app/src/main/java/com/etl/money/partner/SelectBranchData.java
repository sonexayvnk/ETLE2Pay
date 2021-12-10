package com.etl.money.partner;

public class SelectBranchData {
    private String Description;
    private String CirclesTitle;
    private String CirclesColor;

    public SelectBranchData(String CirclesTitle,  String CirclesColor,String Description) {
        this.CirclesTitle = CirclesTitle;
        this.CirclesColor = CirclesColor;
        this.Description = Description;

    }

    public String getCirclesTitle() {
        return CirclesTitle;
    }
    public String getCirclesColor() {
        return CirclesColor;
    }
    public String getDescription() {
        return Description;
    }


}