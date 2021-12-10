package com.etl.money.setting;

import android.graphics.Bitmap;

    public class ItemSetting {
        String textKey;
        Bitmap image;
        String title;
        String descrip;


        public ItemSetting(String textKey, Bitmap image, String title, String descrip) {
            super();
            this.textKey = textKey;
            this.image = image;
            this.title = title;
            this.descrip = descrip;
        }
        public String getTextKey() {
            return textKey;
        }
        public void setTextKey(String textKey) {
            this.textKey = textKey;
        }

        public Bitmap getImage() {
            return image;
        }
        public void setImage(Bitmap image) {
            this.image = image;
        }

        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescrip() {
            return descrip;
        }
        public void setDescrip(String descrip) {
            this.descrip = descrip;
        }


}
