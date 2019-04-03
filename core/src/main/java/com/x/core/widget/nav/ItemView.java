package com.x.core.widget.nav;

import android.view.View;

public class ItemView {
    private View view;
    private boolean clickable = false;

    public ItemView(View view) {
        this.view = view;
    }

    public boolean isClickable() {
        return clickable;
    }

    public void setClickable(boolean clickable) {
        this.clickable = clickable;
    }

    public View getView() {
        return view;
    }
}
