package com.example.saigeetha.homework9.stockdetails;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by SaiGeetha on 4/30/2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                StockDetailsTab tab1 = new StockDetailsTab();
                return tab1;
            case 1:
                HistoricalTab tab2 = new HistoricalTab();
                return tab2;
            case 2:
                NewsFeedTab tab3 = new NewsFeedTab();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
