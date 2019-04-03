package com.x.core.widget.wrv;

import android.view.View;


public interface NewOnAdapterItemStateChangeListener {
	void onStateChanged(AdapterItem item, View v, int... posIndex);
	
}