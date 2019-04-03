package com.x.core.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.x.core.R;
import com.x.core.widget.nav.ActionBarMenu;
import com.x.core.widget.nav.OnActionBarItemSelectedListener;

public abstract class BaseFragment extends Fragment implements OnActionBarItemSelectedListener {
    protected abstract int getContentView();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(getContentView() < 0) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        initGlobalView();
        return mGlobalView;
    }


    protected View actionBarView = null;
    protected ViewGroup mGlobalView = null;
    protected ActionBarMenu mActionBar = null;
    protected volatile int actionBarHeight = 0;
    protected void initGlobalView() {
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getActivity()).inflate(getContentView(),null);
        mActionBar = onActionBarCreate();
        mGlobalView = getRelativeLayout();
        if(mActionBar != null) {
            LinearLayout myLinearLayout = getLinearLayout();
            actionBarView = inflateActionBarView();
            actionBarHeight = actionBarView.getHeight();
            mActionBar.setViewAndListener(actionBarView, this);
            myLinearLayout.addView(actionBarView);

            ViewGroup.LayoutParams lp1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            myLinearLayout.addView(viewGroup,lp1);
            mGlobalView = myLinearLayout;
        }else {
            mGlobalView = viewGroup;
        }
    }
    protected View inflateActionBarView() {
        return LayoutInflater.from(getActivity()).inflate(R.layout.layout_actionbar, null);
    }

    private LinearLayout getLinearLayout() {
        LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, height);
        ll.setLayoutParams(lp);
        return ll;
    }

    private RelativeLayout getRelativeLayout() {
        RelativeLayout ll = new RelativeLayout(getActivity());
        int width = RelativeLayout.LayoutParams.MATCH_PARENT;
        int height = RelativeLayout.LayoutParams.MATCH_PARENT;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
        ll.setLayoutParams(params);

        return ll;
    }

    protected ActionBarMenu onActionBarCreate(){
        return null;
    }

    @Override
    public void onActionBarClick(View v) {}

    public ActionBarMenu getActionBarMenu() {
        return mActionBar;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
