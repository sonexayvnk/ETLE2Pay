package com.etl.money.promotion_and_advertising;

/**
 * Created by Kuncoro on 29/02/2016.
 */
public class PromotionData {
    private String  txtDescription;
    private String  txtDetail;
    private String  txtInfo;
    private String  imgBase64;

    public String gettxtDescription() {return txtDescription;}
    public void settxtDescription(String txtDescription) {this.txtDescription = txtDescription;}
    public String gettxtDetail() {return txtDetail;}
    public void settxtInfo(String txtInfo) {this.txtInfo = txtInfo;}
    public String gettxtInfo() {return txtInfo;}
    public void settxtDetail(String txtDetail) {this.txtDetail = txtDetail;}
    public String getimgBase64() {return imgBase64;}
    public void setimgBase64(String imgBase64) {this.imgBase64 = imgBase64;}

}