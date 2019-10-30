package com.woojeong.global.ifriend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.woojeong.global.ifriend.DTO.ChatList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Main2ThirdFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Main2ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Main2ThirdFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "Main2ThirdFragment";

    AQuery aQuery = null;
    Context context;
    View view;
    String token;
    InputMethodManager ipmm;

    private OnFragmentInteractionListener mListener;

    TextView hidden;
    LinearLayout hidden_con;
    ImageView  kakao_btn;

    ListView chat_list;
    ArrayList<ChatList> data;
    ChatListAdapter chatListAdapter;

    Parcelable state;

    public Main2ThirdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Main2ThirdFragment newInstance(String param1, String param2) {
        Main2ThirdFragment fragment = new Main2ThirdFragment();
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
        view = inflater.inflate(R.layout.fragment_main2_third, container, false);
        context = getContext();
        aQuery = new AQuery(context);
        ipmm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        SharedPreferences prefToken = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");

        kakao_btn = view.findViewById(R.id.kakao_btn);

        hidden = view.findViewById(R.id.hidden);
        hidden_con = view.findViewById(R.id.hidden_con);
        chat_list = view.findViewById(R.id.chat_list);

        kakao_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://pf.kakao.com/_HjGwj"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        hidden_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hidden.callOnClick();
            }
        });
        hidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HiddenMessageActivity.class);
                startActivity(intent);
            }
        });

        data = new ArrayList<ChatList>();
        chatListAdapter = new ChatListAdapter(context, R.layout.list_chat_item, data, chat_list);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reload();
    }

    void pushOn(){
        state = chat_list.onSaveInstanceState();
        reload();
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
        final String url = ServerUrl.getBaseUrl() + "/chat/mlist";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("hidden", "0");
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    data.clear();
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONArray jsonArray = new JSONArray(jsonObject.getString("list"));
                        if(jsonArray.length()==0){
                            chat_list.setVisibility(View.GONE);
                            Toast.makeText(context, "대화 목록이 없습니다.", Toast.LENGTH_SHORT).show();
                        }else{
                            chat_list.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject getJsonObject = jsonArray.getJSONObject(i);
                                data.add(new ChatList(getJsonObject.getString("idx"), getJsonObject.getString("profile"), getJsonObject.getString("name"), getJsonObject.getString("content"), getJsonObject.getString("date"), getJsonObject.getString("new")));
                            }
                            chat_list.setAdapter(chatListAdapter);
                            chatListAdapter.notifyDataSetChanged();
                        }
                    } else if (!jsonObject.getBoolean("return")) {
                        chat_list.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
    }
}
