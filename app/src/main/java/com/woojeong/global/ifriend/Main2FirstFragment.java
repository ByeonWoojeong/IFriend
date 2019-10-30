package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.woojeong.global.ifriend.DTO.Main;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Main2FirstFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Main2FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main2FirstFragment extends Fragment {
    private static final String TAG = "Main2FirstFragment";
    Main2Activity main2Activity;
    AQuery aQuery = null;
    Context context;
    View view;
    String token;
    private OnFragmentInteractionListener mListener;
    BackPressCloseHandler backPressCloseHandler;
    InputMethodManager ipmm;
    Parcelable state;
    TabLayout tabLayout;
    String tabChoice = "1";
    View viewRequest, viewApproved, viewVisited, viewCancel;
    ViewPager viewPager;
    FragmentManager fragmentManager;
    Main2ReservationPagerAdapter main2Adapter;
    AlphaAnimation fade_in, fade_out;
    OneBtnDialog oneBtnDialog;

    public Main2FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFirstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Main2FirstFragment newInstance(String param1, String param2) {
        Main2FirstFragment fragment = new Main2FirstFragment();
        Bundle args = new Bundle();
        args.putString("some_int", param1);
        args.putString("some_title", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main2_first, container, false);
        context = getContext();
        aQuery = new AQuery(context);
        SharedPreferences prefToken = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
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

        final TextView a, b, c, d;
        viewRequest = getLayoutInflater().inflate(R.layout.custom_tab_reservation, null);
        a = viewRequest.findViewById(R.id.title);
        a.setText("승인 요청");
        a.setTextColor(getResources().getColor(R.color.subTitleGray));
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewRequest));

        viewApproved = getLayoutInflater().inflate(R.layout.custom_tab_reservation, null);
        b = viewApproved.findViewById(R.id.title);
        b.setText("승인 완료");
        b.setTextColor(getResources().getColor(R.color.subTitleGray));
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewApproved));

        viewVisited = getLayoutInflater().inflate(R.layout.custom_tab_reservation, null);
        c = viewVisited.findViewById(R.id.title);
        c.setText("방문 완료");
        c.setTextColor(getResources().getColor(R.color.subTitleGray));
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewVisited));

        viewCancel = getLayoutInflater().inflate(R.layout.custom_tab_reservation, null);
        d = viewCancel.findViewById(R.id.title);
        d.setText("예약 취소");
        d.setTextColor(getResources().getColor(R.color.subTitleGray));
        tabLayout.addTab(tabLayout.newTab().setCustomView(viewCancel));

        tabLayout.getTabAt(0).setText("승인 요청");
        a.setTextColor(getResources().getColor(R.color.mainColor));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        viewPager = (ViewPager) view.findViewById(R.id.pager_reservation);
        fragmentManager = getActivity().getSupportFragmentManager();
        main2Adapter = new Main2ReservationPagerAdapter(getContext(), fragmentManager, tabLayout.getTabCount());
        viewPager.setAdapter(main2Adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(4);
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                ipmm.hideSoftInputFromWindow(viewPager.getWindowToken(), 0);
                if (tab.getPosition() == 0) {
                    viewRequest.setSelected(true);
                    viewApproved.setSelected(false);
                    viewVisited.setSelected(false);
                    viewCancel.setSelected(false);
                    tabChoice = "1";
                    a.setTextColor(getResources().getColor(R.color.mainColor));
                    b.setTextColor(getResources().getColor(R.color.subTitleGray));
                    c.setTextColor(getResources().getColor(R.color.subTitleGray));
                    d.setTextColor(getResources().getColor(R.color.subTitleGray));
                } else if (tab.getPosition() == 1) {
                    viewRequest.setSelected(false);
                    viewApproved.setSelected(true);
                    viewVisited.setSelected(false);
                    viewCancel.setSelected(false);
                    tabChoice = "2";
                    b.setTextColor(getResources().getColor(R.color.mainColor));
                    a.setTextColor(getResources().getColor(R.color.subTitleGray));
                    c.setTextColor(getResources().getColor(R.color.subTitleGray));
                    d.setTextColor(getResources().getColor(R.color.subTitleGray));
                    SharedPreferences pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked){
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
//                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    }
                } else if (tab.getPosition() == 2) {
                    viewRequest.setSelected(false);
                    viewApproved.setSelected(false);
                    viewVisited.setSelected(true);
                    viewCancel.setSelected(false);
                    tabChoice = "3";
                    c.setTextColor(getResources().getColor(R.color.mainColor));
                    a.setTextColor(getResources().getColor(R.color.subTitleGray));
                    b.setTextColor(getResources().getColor(R.color.subTitleGray));
                    d.setTextColor(getResources().getColor(R.color.subTitleGray));
                    SharedPreferences pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked){
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
//                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    } else {
//                        if ("sitter".equals(mode)) {
//                            MainThirdFragment thirdFragment = (MainThirdFragment) fragmentManager.findFragmentByTag("third");
////                            thirdFragment.refresh_data();
//                        } else if ("i".equals(mode)) {
//                            MainThirdFragment thirdFragment2 = (MainThirdFragment) fragmentManager.findFragmentByTag("third2");
////                            thirdFragment2.refresh_data();
//                        }
                    }
                } else if (tab.getPosition() == 3) {
                    viewRequest.setSelected(false);
                    viewApproved.setSelected(false);
                    viewVisited.setSelected(false);
                    viewCancel.setSelected(true);
                    tabChoice = "4";
                    d.setTextColor(getResources().getColor(R.color.mainColor));
                    a.setTextColor(getResources().getColor(R.color.subTitleGray));
                    b.setTextColor(getResources().getColor(R.color.subTitleGray));
                    c.setTextColor(getResources().getColor(R.color.subTitleGray));
                    SharedPreferences pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked){
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
//                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    } else {

                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    tabChoice = "1";
                } else if(tab.getPosition()==1){
                    SharedPreferences pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked){
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    } else {
                        tabChoice = "2";
                    }
                } else if(tab.getPosition()==2){
                    SharedPreferences pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked){
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    } else {
                        tabChoice = "3";
                    }
                } else if(tab.getPosition()==3){
                    SharedPreferences pref = context.getSharedPreferences("pref", Activity.MODE_PRIVATE);
                    boolean getLoginChecked = pref.getBoolean("loginChecked", false);
                    if (!getLoginChecked){
//                        Intent getIntent = new Intent(MainActivity.this, LoginActivity.class);
//                        startActivityForResult(getIntent, 1);
                        tabChoice = "1";
//                        getViewPager().setCurrentItem(0);
                    } else {
                        tabChoice = "4";
                    }
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        main2Adapter.notifyDataSetChanged();
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

    @Override
    public void onStop() {
        super.onStop();
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
