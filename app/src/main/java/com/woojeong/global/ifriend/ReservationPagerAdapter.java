package com.woojeong.global.ifriend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class ReservationPagerAdapter extends FragmentStatePagerAdapter {
    Context context;
    int numOfTabs;
    FragmentManager fragmentManager;
    ReservationNowFragment reservationNowFragment;
    ReservationLastFragment reservationLastFragment;
    public ReservationPagerAdapter(Context context, FragmentManager fragmentManager, int numOfTabs) {
        super(fragmentManager);
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.numOfTabs = numOfTabs;
    }

    public void reload(String tab){
        Log.d("ReservationNowFragment", "reload: "+tab);
        if(tab.equals("1")){
            if(reservationNowFragment != null)
                reservationNowFragment.showList();
        }else if (tab.equals("2")){
            if(reservationLastFragment != null)
                reservationLastFragment.showList();
        }
    }

    @Override   //i에 해당하는 fragment를 반환.
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                reservationNowFragment = new ReservationNowFragment();
                fragmentManager.beginTransaction().add(reservationNowFragment, "first");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return reservationNowFragment;
            case 1:
                reservationLastFragment = new ReservationLastFragment();
                fragmentManager.beginTransaction().add(reservationLastFragment, "second");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return reservationLastFragment;

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
