package kj.x.recruit;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import java.util.ArrayList;
import butterknife.BindView;
import kj.x.recruit.adapter.HomePageAdapterVP;
import kj.x.recruit.base.SupperActivity;
import kj.x.recruit.fragment.HomeFragment;
import kj.x.recruit.tab.TabEntity;

public class MainActivity extends SupperActivity {
    @BindView(R.id.tl_1)
    CommonTabLayout tabLayout;

    @BindView(R.id.vp_2)
    ViewPager viewPager;

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initApp();
    }

    private void initApp() {
        mTabEntities.add(new TabEntity("首页", R.mipmap.tab_home_select, R.mipmap.tab_home_unselect, new HomeFragment()));
        mTabEntities.add(new TabEntity("我的", R.mipmap.tab_more_select, R.mipmap.tab_more_unselect, new HomeFragment()));
        tabLayout.setTabData(mTabEntities);

        viewPager.setAdapter(new HomePageAdapterVP(getSupportFragmentManager(), mTabEntities));
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tabLayout.setCurrentTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
}
