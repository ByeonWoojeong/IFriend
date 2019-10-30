package com.woojeong.global.ifriend;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MainPagerAdapter extends FragmentStatePagerAdapter {
    Context context;
    FragmentManager fragmentManager;
    int numOfTabs;
    MainFirstFragment firstFragment;
    MainSecondFragment secondFragment;
    MainThirdFragment thirdFragment;
    MainFourthFragment fourthFragment;
    MainFifthFragment fifthFragment;
    MainPagerAdapter mainPagerAdapter = this;


    public MainPagerAdapter(Context context, FragmentManager fragmentManager, int numOfTabs) {
        super(fragmentManager);
        this.context = context;
        this.fragmentManager = fragmentManager;
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int i) {
        switch (i) {
            case 0:
                firstFragment = new MainFirstFragment();
                fragmentManager.beginTransaction().add(firstFragment, "first");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return firstFragment;
            case 1:
                secondFragment = new MainSecondFragment();
                fragmentManager.beginTransaction().add(secondFragment, "second");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return secondFragment;
            case 2:

                thirdFragment = new MainThirdFragment();
                fragmentManager.beginTransaction().add(thirdFragment, "third");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return thirdFragment;

            case 3:
                fourthFragment = new MainFourthFragment();
                fragmentManager.beginTransaction().add(fourthFragment, "fourth");
                fragmentManager.beginTransaction().addToBackStack(null);
                fragmentManager.beginTransaction().commitAllowingStateLoss();
                fragmentManager.beginTransaction().commit();
                return fourthFragment;
            case 4:
                fifthFragment = new MainFifthFragment();
                return fifthFragment;
            default:
                return null;

        }
    }

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
