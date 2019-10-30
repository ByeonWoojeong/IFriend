package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.TextView;

import com.androidquery.AQuery;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainSecondFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainSecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainSecondFragment extends Fragment {
    private static String TAG = "MainSecondFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    MainActivity mainActivity;
    AQuery aQuery = null;
    Context context;
    View view;
    String token;
    BackPressCloseHandler backPressCloseHandler;
    InputMethodManager ipmm;
    Parcelable state;
    TabLayout tabLayout;
    String tabChoice = "1";
    View viewNow, viewLast;
    ViewPager viewPager;
    FragmentManager fragmentManager;
    ReservationPagerAdapter reservationPagerAdapter;

    private OnFragmentInteractionListener mListener;

    AlphaAnimation fade_in, fade_out;
    WebView webView;
    SharedPreferences prefToken;
    OneBtnDialog oneBtnDialog;

    public MainSecondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainSecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainSecondFragment newInstance(String param1, String param2) {
        MainSecondFragment fragment = new MainSecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_second, container, false);
        context = getContext();
        prefToken = getActivity().getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");
        aQuery = new AQuery(getContext());
        fade_in = new AlphaAnimation(0.0f, 1.0f);
        fade_out = new AlphaAnimation(1.0f, 0.0f);
        fade_in.setDuration(300);
        fade_out.setDuration(300);
        backPressCloseHandler = new BackPressCloseHandler(getActivity());
        //뒤로가기(BackButton Listener) : backPressCloseHandler.onBackPressed();
        ipmm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //키보드 숨기기 : ipmm.hideSoftInputFromWindow(getWindow().getDecorView().getRootView().getWindowToken(), 0);

        tabLayout = (TabLayout) view.findViewById(R.id.reservation_tabs);

        final TextView a, b;
        viewNow = getLayoutInflater().inflate(R.layout.custom_tab_reservation, null);
        a = viewNow.findViewById(R.id.title);
        a.setText("현재 예약");
        a.setTextColor(getResources().getColor(R.color.subTitleGray));
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewNow));

        viewLast = getLayoutInflater().inflate(R.layout.custom_tab_reservation, null);
        b = viewLast.findViewById(R.id.title);
        b.setText("지난 예약");
        b.setTextColor(getResources().getColor(R.color.subTitleGray));
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewLast));

        tabLayout.getTabAt(0).setText("현재 예약");
        a.setTextColor(getResources().getColor(R.color.mainColor));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) view.findViewById(R.id.pager_reservation);
        fragmentManager = getActivity().getSupportFragmentManager();
        reservationPagerAdapter = new ReservationPagerAdapter(getContext(), fragmentManager, tabLayout.getTabCount());
        viewPager.setAdapter(reservationPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        viewPager.addOnPageChangeListener (new ViewPager.OnPageChangeListener() {
//            public void onPageScrollStateChanged(int arg0) {
//            }
//
//            public void onPageScrolled(int arg0, float arg1, int arg2) {
//            }
//
//            public void onPageSelected(int currentPage) {
//                for(int i=0; i<tabLayout.getTabCount(); i++){
//                    if (i == currentPage) {
//                        tabLayout.getTabAt(
//                    } else {
//                    }
//                }
//            }
//        });
        viewPager.setOffscreenPageLimit(2);

        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                ipmm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                if (tab.getPosition() == 0) {
                    viewNow.setSelected(true);
                    viewLast.setSelected(false);
                    tabChoice = "1";
                    a.setTextColor(getResources().getColor(R.color.mainColor));
                    b.setTextColor(getResources().getColor(R.color.subTitleGray));
                } else if (tab.getPosition() == 1) {
                    viewNow.setSelected(false);
                    viewLast.setSelected(true);
                    tabChoice = "2";
                    b.setTextColor(getResources().getColor(R.color.mainColor));
                    a.setTextColor(getResources().getColor(R.color.subTitleGray));
                    SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked) {
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tabChoice = "1";
                } else if (tab.getPosition() == 1) {
                    SharedPreferences pref = getActivity().getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked) {
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    } else {
                        tabChoice = "2";
                    }
                }
            }
        });
        reload();
        return view;
    }


    public void reload(){
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                reservationPagerAdapter.reload(tabChoice);
            }
        }, 500);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(prefToken.getString("tab","").equals("2")){
            reload();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public ViewPager getViewPager() {
        if (null == viewPager) {
            viewPager = view.findViewById(R.id.main_viewpager);
        }
        final ReservationPagerAdapter adapter = new ReservationPagerAdapter(getContext(), getActivity().getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        return viewPager;
    }

    public class OneBtnDialog extends Dialog {
        OneBtnDialog oneBtnDialog = this;
        Context context;

        public OneBtnDialog(final Context context, final String text, final String btnText) {
            super(context);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_one_btn);
            getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            this.context = context;
            TextView title1 = (TextView) findViewById(R.id.title1);
            TextView title2 = (TextView) findViewById(R.id.title2);
            TextView btn1 = (TextView) findViewById(R.id.btn1);
            title2.setVisibility(View.GONE);
            title1.setText(text);
            btn1.setText(btnText);
            btn1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    oneBtnDialog.dismiss();
                }
            });
        }
    }
}
