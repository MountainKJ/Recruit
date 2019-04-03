package com.x.core.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.x.core.R;
import com.x.core.widget.nav.ActionBarMenu;
import com.x.core.widget.nav.OnActionBarItemSelectedListener;

public abstract class BaseActivity extends AppCompatActivity implements OnActionBarItemSelectedListener {
    protected final String TAG = this.getClass().getSimpleName();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initGlobalView();
        setContentView(mGlobalView);
        initGoBack();
    }

    protected abstract int getContentView();

    /** 当点击返回按钮时调用 */
    protected void onGoBack() {
        this.finish();
    }
    private void initGoBack() {
        View goBackView = getGoBackView();
        if (goBackView != null) {
            goBackView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onGoBack();
                }
            });
        }
    }

    protected View getGoBackView() {
        ActionBarMenu bar = getActionBarMenu();
        if(bar != null){
            return bar.getLeftLayout();
        }
        return null;
    }

    protected final ActionBarMenu getActionBarMenu(){
        return this.mActionBar;
    }

    protected ActionBarMenu onActionBarCreate(){
        return null;
    }
    protected ViewGroup mGlobalView = null;
    protected ActionBarMenu mActionBar = null;
    protected View actionBarView = null;
    protected void initGlobalView() {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getActivity()).inflate(getContentView(), null);
        mActionBar = onActionBarCreate();
        if (mActionBar != null) {
            LinearLayout myLinearLayout = getLinearLayout();
            actionBarView = inflateActionBarView();
            mActionBar.setViewAndListener(actionBarView, this);
            myLinearLayout.addView(actionBarView);

            ViewGroup.LayoutParams lp1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            myLinearLayout.addView(viewGroup,lp1);
            mGlobalView = myLinearLayout;
        } else {
            mGlobalView = viewGroup;
        }

    }

    @Override
    public void onActionBarClick(View v) {

    }
    protected View inflateActionBarView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.layout_actionbar, null);
    }

    private Activity getActivity(){
        return this;
    }

    private LinearLayout getLinearLayout() {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        ll.setLayoutParams(params);
        return ll;
    }

    protected void jumpAct(Context fromCtx, Class targetClazz) {
        startActivity(new Intent(fromCtx, targetClazz));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
