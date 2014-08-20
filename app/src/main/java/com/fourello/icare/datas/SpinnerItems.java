package com.fourello.icare.datas;

public class SpinnerItems {
    public String spinner_title;
    public Boolean status;

    public SpinnerItems() {

    }

    public SpinnerItems(String spinner_title, boolean status) {
        super();
        this.spinner_title = spinner_title;
        this.status = status;
    }

    public String getSpinnerTitle() {
        return spinner_title;
    }

    @Override
    public String toString() {
        return getSpinnerTitle();
    }

    public Boolean getSpinnerStatus() {
        return status;
    }
}
