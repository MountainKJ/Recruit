package kj.x.recruit.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.ConvertUtils;
import com.x.core.widget.nav.ActionBarMenu;

import butterknife.ButterKnife;
import kj.x.recruit.R;

public abstract class SupperActivity extends AppCompatActivity {
    protected abstract int getContentView();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);
//        processActionBarMenu(getActionBarMenu());
    }

    public static void processActionBarMenu(ActionBarMenu bar) {
        if (bar == null)
            return;
        if (bar.getIcon() <= 0) {
            ImageView iv = bar.getIconView();
            if (iv != null) {
                iv.setVisibility(View.VISIBLE);
                iv.setImageResource(R.mipmap.back_arrow_ffffff);
                final int dp4 = ConvertUtils.dp2px(11);
                final int dp5 = ConvertUtils.dp2px(15);
                iv.setPadding(dp5, dp4, dp5, dp4);
            }
        }
        bar.getTitleView().setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
    }
}
