package com.etl.money.PackageData;

public class DataModel {
    private String package_name, data_price, package_more_detail, imgURL, imghit,textCircle,register;

    public String getTextCircle() {
        return textCircle;
    }

    public void setTextCircle(String textCircle) {
        this.textCircle = textCircle;
    }

    public String getImghit() {
        return imghit;
    }

    public void setImghit(String imghit) {
        this.imghit = imghit;
    }

    public String getImgURL(){
        return imgURL;
    }


    public String getregister() {
        return register;
    }

    public void setregister(String register) {
        this.register = register;
    }

    public void setImgURL(String imgURL){
        this.imgURL = imgURL;
    }



    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }




    public String getData_price() {
        return data_price;
    }


    public void setData_price(String data_price) {
        this.data_price = data_price;
    }

    public String getPackage_more_detail() {
        return package_more_detail;
    }

    public void setPackage_more_detail(String package_more_detail) {
        this.package_more_detail = package_more_detail;
    }
}