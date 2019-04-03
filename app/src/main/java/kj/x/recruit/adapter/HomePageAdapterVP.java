package kj.x.recruit.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.List;
import kj.x.recruit.tab.TabEntity;

public class HomePageAdapterVP extends FragmentPagerAdapter {
    private List<CustomTabEntity> tabEntities;

    public HomePageAdapterVP(FragmentManager fm, List<CustomTabEntity> tabEntities) {
        super(fm);
        this.tabEntities = tabEntities;
    }

    @Override
    public Fragment getItem(int i) {
        TabEntity te = (TabEntity) tabEntities.get(i);
        return te.getFg();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        TabEntity te = (TabEntity) tabEntities.get(position);
        return te.getTabTitle();
    }

    @Override
    public int getCount() {
        return tabEntities == null ? 0 : tabEntities.size();
    }
}
