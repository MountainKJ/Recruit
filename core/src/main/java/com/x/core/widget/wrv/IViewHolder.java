package com.x.core.widget.wrv;

import android.view.View;

public interface IViewHolder {
    void setItem(AdapterItem item);

    AdapterItem getItem();

    void bindItem();

    void refreshView();

    View getRootView();

    void initViews();

    void recycleItem();

    void destroy();
}
