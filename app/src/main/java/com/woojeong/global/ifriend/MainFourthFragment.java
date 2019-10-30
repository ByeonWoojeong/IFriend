package com.woojeong.global.ifriend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.JournalList;
import com.woojeong.global.ifriend.DTO.SearchName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFourthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFourthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFourthFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "MainFourthFragment";

    AQuery aQuery = null;
    Context context;
    View view;
    String token;

    private OnFragmentInteractionListener mListener;

    LinearLayout no_data_con;
    ImageView  no_data_icon;
    TextView no_data;

    ListView journal_list;

    JournalListAdapter journalListAdapter;
    ArrayList<JournalList> data;
    SharedPreferences prefToken;

    public MainFourthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFourthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFourthFragment newInstance(String param1, String param2) {
        MainFourthFragment fragment = new MainFourthFragment();
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
        view = inflater.inflate(R.layout.fragment_main_fourth, container, false);
        context = getContext();
        aQuery = new AQuery(context);
        prefToken = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");

        no_data_con = view.findViewById(R.id.no_data_con);
        no_data_icon = view.findViewById(R.id.no_data_icon);
        no_data = view.findViewById(R.id.no_data);

        journal_list = view.findViewById(R.id.journal_list);
        data = new ArrayList<JournalList>();
        journalListAdapter = new JournalListAdapter(context, R.layout.list_journal_item, data, journal_list, "mom");
        reload();
        return view;
    }


    public void reload(){
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                showList();
            }
        }, 500);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(prefToken.getString("tab", "").equals("4")){
            reload();
        }
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

    void showList(){
        SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/diary/list";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", "2");
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    data.clear();
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        if (jsonArray.length() == 0) {
                            journal_list.setVisibility(View.GONE);
                            return;
                        } else {
                            journal_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                data.add(new JournalList(getJsonObject.getString("reserve"), getJsonObject.getString("friend_idx"), getJsonObject.getString("friend_name"), getJsonObject.getString("friend_profile"), getJsonObject.getString("status"), getJsonObject.getString("period"), getJsonObject.getString("reviewm")));
                            }
                            journal_list.setAdapter(journalListAdapter);
                            journalListAdapter.notifyDataSetChanged();
                        }
                    } else if (!jsonObject.getBoolean("return")) {
                        journal_list.setVisibility(View.GONE);
                        if("login".equals(jsonObject.getString("type"))){

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
    }
}
