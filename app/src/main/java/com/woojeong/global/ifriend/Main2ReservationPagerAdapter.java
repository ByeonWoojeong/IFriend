package com.woojeong.global.ifriend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class Main2ReservationPagerAdapter extends FragmentStatePagerAdapter {
    Context context;
    int numOfTabs;
    FragmentManager fragmentManager;
    ReservationNowFragment reservationNowFragment;
    ReservationLastFragment reservationLastFragment;

    Main2RequestFragment requestFragment;
    Main2ApprovedFragment approvedFragment;
    Main2VisitedFragment visitedFragment;
    Main2CancelFragment cancelFragment;
    public Main2ReservationPagerAdapter(Context context, FragmentManager fragmentManager, int numOfTabs) {
        super(fragmentManager);
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.numOfTabs = numOfTabs;
    }

    @Override   //i에 해당하는 fragment를 반환.
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                requestFragment = new Main2RequestFragment();
                fragmentManager.beginTransaction().add(requestFragment, "first");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return requestFragment;
            case 1:
                approvedFragment = new Main2ApprovedFragment();
                fragmentManager.beginTransaction().add(approvedFragment, "second");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return approvedFragment;
            case 2:
                visitedFragment = new Main2VisitedFragment();
                fragmentManager.beginTransaction().add(visitedFragment, "second");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return visitedFragment;
            case 3:
                cancelFragment = new Main2CancelFragment();
                fragmentManager.beginTransaction().add(cancelFragment, "second");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return cancelFragment;

            default:
                return null;
        }
    }

//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return POSITION_NONE;
//    }

    @Override   //페이지의 개수를 반환. 반환되는 수에 따라 페이지의 수가 결정됨.
    public int getCount() {
        return numOfTabs;
    }
}
