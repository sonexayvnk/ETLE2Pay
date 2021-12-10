package com.etl.money.dashboard;

public class DashboardPersonUtils {
    private String txtdash1, txtdash2, txtdash3 ;


    public DashboardPersonUtils(String txtdash1) {
        this.txtdash1 = txtdash1;
        this.txtdash2 = txtdash2;
        this.txtdash3 = txtdash3;
    }


    public String gettxtdash1() {
        return txtdash1;
    }
    public void settxtdash1(String txtdash) {
        this.txtdash1 = txtdash;
    }

    public String gettxtdash2() {
        return txtdash2;
    }
    public void settxtdash2(String txtdash2) {
        this.txtdash2 = txtdash2;
    }

    public String gettxtdash3() {
        return txtdash3;
    }
    public void settxtdash3(String txtdash3) {
        this.txtdash3 = txtdash3;
    }
}