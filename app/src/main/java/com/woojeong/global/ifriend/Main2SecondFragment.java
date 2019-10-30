package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Main2SecondFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Main2SecondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main2SecondFragment extends Fragment {
    private static String TAG = "Main2SecondFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    AQuery aQuery = null;
    Context context;
    View view;
    String token;

    private OnFragmentInteractionListener mListener;

    MaterialCalendarView calendar_view;
    FrameLayout edit_con;
    TextView edit;

    OneBtnDialog oneBtnDialog;

    ArrayList<String> dateList;

    Intent getIntent;
    String getMember;

    public Main2SecondFragment() {
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
    public static Main2SecondFragment newInstance(String param1, String param2) {
        Main2SecondFragment fragment = new Main2SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main2_second, container, false);
        context = getContext();
        aQuery = new AQuery(context);
        SharedPreferences prefToken = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");

        calendar_view = view.findViewById(R.id.calendar_view);
        edit_con = view.findViewById(R.id.edit_con);
        edit = view.findViewById(R.id.edit);

        edit_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit.callOnClick();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AdjustScheduleActivity.class);
                intent.putExtra("dateSetting", dateList);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        viewSettings();
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

    void viewSettings() {
        calendar_view.clearSelection();
        final SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/profile/getdate";
        Map<String, Object> params = new HashMap<String, Object>();
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " jsonObject " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        final JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = new JSONArray(jsonData.getString("date"));
                        Log.i(TAG, " jsonArray " + jsonArray);
                        if(jsonArray.length()==0){
                            Toast.makeText(context, "불가능한 날짜를 지정해주세요.",Toast.LENGTH_SHORT).show();
                        }else{

                            dateList = new ArrayList<String>();
                            dateList.clear();
                            for (int i=0; i<jsonArray.length(); i++) {
                                dateList.add( jsonArray.getString(i) );
                                int year = Integer.parseInt(dateList.get(i).substring(0,4));
                                int month = Integer.parseInt(dateList.get(i).substring(5,7));
                                if("0".equals(dateList.get(i).indexOf(5))){
                                    month = Integer.parseInt(dateList.get(i).substring(6,7));
                                }
                                int day = Integer.parseInt(dateList.get(i).substring(8,10));
                                if("0".equals(dateList.get(i).indexOf(8))){
                                    day = Integer.parseInt(dateList.get(i).substring(9,10));
                                }

                                calendar_view.setSelectionColor(getResources().getColor(R.color.subTitleGray));
                                CalendarDay calendarDay = CalendarDay.from(year, month, day);
                                calendar_view.setDateSelected(calendarDay, true);
                            }

                        }

                    } else if (!jsonObject.getBoolean("return")) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
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
