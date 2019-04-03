package kj.x.recruit.tab;

import android.support.v4.app.Fragment;

import com.flyco.tablayout.listener.CustomTabEntity;

public class TabEntity implements CustomTabEntity {
    private String title;
    private int selectedIcon;
    private int unSelectedIcon;
    private Fragment fg;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon, Fragment fg) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
        this.fg = fg;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }

    public Fragment getFg() {
        return fg;
    }

    public void setFg(Fragment fg) {
        this.fg = fg;
    }
}
