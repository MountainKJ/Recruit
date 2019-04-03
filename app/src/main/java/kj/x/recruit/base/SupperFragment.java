package kj.x.recruit.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ConvertUtils;
import com.x.core.base.BaseFragment;
import com.x.core.widget.nav.ActionBarMenu;

import kj.x.recruit.R;

public abstract class SupperFragment extends BaseFragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        processActionBarMenu(getActionBarMenu());
    }

    private static int px = 0;
    private void processActionBarMenu(ActionBarMenu bar) {
        if (bar == null)
            return;
        if (bar.getIcon() <= 0) {
            ImageView iv = bar.getIconView();
            iv.setVisibility(View.INVISIBLE);
        }else{
            ImageView iv = bar.getIconView();
            if (iv != null) {
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(R.mipmap.back_arrow_ffffff);
                if (px == 0) {
                    px = ConvertUtils.dp2px(10);
                }
                iv.setPadding(0, px, 0, px);
            }
        }
    }
}
