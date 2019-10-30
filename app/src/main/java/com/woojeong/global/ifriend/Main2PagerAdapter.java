package com.woojeong.global.ifriend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class Main2PagerAdapter extends FragmentStatePagerAdapter {
    Context context;
    FragmentManager fragmentManager;
    int numOfTabs;
    Main2FirstFragment firstFragment;
    Main2SecondFragment secondFragment;
    Main2ThirdFragment thirdFragment;
    Main2FourthFragment fourthFragment;
    Main2FifthFragment fifthFragment;

    public Main2PagerAdapter(Context context, FragmentManager fragmentManager, int numOfTabs) {
        super(fragmentManager);
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                firstFragment = new Main2FirstFragment();
                fragmentManager.beginTransaction().add(firstFragment, "first");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return firstFragment;
            case 1:
                secondFragment = new Main2SecondFragment();
                fragmentManager.beginTransaction().add(secondFragment, "second");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return secondFragment;
            case 2:

                thirdFragment = new Main2ThirdFragment();
                fragmentManager.beginTransaction().add(thirdFragment, "third");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return thirdFragment;

            case 3:
                fourthFragment = new Main2FourthFragment();
                fragmentManager.beginTransaction().add(fourthFragment, "fourth");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return fourthFragment;
            case 4:
                fifthFragment = new Main2FifthFragment();
                return fifthFragment;
            default:
                return null;

        }
    }

//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return POSITION_NONE;
//    }

    public Fragment getFragment(int i) {
        switch (i) {
            case 0:
                return firstFragment;
            case 1:
                return secondFragment;
            case 2:
                return thirdFragment;

            case 3:
                return fourthFragment;
            case 4:
                return fifthFragment;
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
