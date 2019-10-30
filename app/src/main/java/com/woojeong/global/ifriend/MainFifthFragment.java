package com.woojeong.global.ifriend;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFifthFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFifthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFifthFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = "MainFifthFragment";

    AQuery aQuery = null;
    Context context;
    View view;
    String token;

    OneBtnDialog oneBtnDialog;

    private OnFragmentInteractionListener mListener;

    ImageView my_img, edit_profile, mode_switch_icon;
    TextView name, my_child_profile, like_sitter, notice, helper, one_to_one, preferences, about_service, refund_policy, sitter_support, mode_switch;
    LinearLayout my_child_profile_con, like_sitter_con, notice_con, helper_con, one_to_one_con, preferences_con, about_service_con, refund_policy_con, sitter_support_con, mode_con, new_con1, new_con2;
    ImageView new_badge_con1, new_badge_con2;
    FrameLayout img_con, mode_switch_con;

    String role = "mom";
    String mode = "", support = "";
    SharedPreferences prefToken;
    public MainFifthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFifthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFifthFragment newInstance(String param1, String param2) {
        MainFifthFragment fragment = new MainFifthFragment();
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
        view = inflater.inflate(R.layout.fragment_main_fifth, container, false);

        context = getContext();
        aQuery = new AQuery(context);
        prefToken = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        token = prefToken.getString("Token", "");

        my_img = view.findViewById(R.id.my_img);
        edit_profile = view.findViewById(R.id.edit_profile);
        mode_switch_icon = view.findViewById(R.id.mode_switch_icon);

        name = view.findViewById(R.id.name);
        my_child_profile = view.findViewById(R.id.my_child_profile);
        like_sitter = view.findViewById(R.id.like_sitter);
        notice = view.findViewById(R.id.notice);
        helper = view.findViewById(R.id.helper);
        one_to_one = view.findViewById(R.id.one_to_one);
        preferences = view.findViewById(R.id.preferences);
        about_service = view.findViewById(R.id.about_service);
        refund_policy = view.findViewById(R.id.refund_policy);
        sitter_support = view.findViewById(R.id.sitter_support);
        mode_switch = view.findViewById(R.id.mode_switch);

        sitter_support.setPaintFlags(sitter_support.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
        mode_switch.setPaintFlags(mode_switch.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

        my_child_profile_con = view.findViewById(R.id.my_child_profile_con);
        like_sitter_con = view.findViewById(R.id.like_sitter_con);
        notice_con = view.findViewById(R.id.notice_con);
        helper_con = view.findViewById(R.id.helper_con);
        one_to_one_con = view.findViewById(R.id.one_to_one_con);
        preferences_con = view.findViewById(R.id.preferences_con);
        about_service_con = view.findViewById(R.id.about_service_con);
        refund_policy_con = view.findViewById(R.id.refund_policy_con);
        sitter_support_con = view.findViewById(R.id.sitter_support_con);
        mode_con = view.findViewById(R.id.mode_con);
        new_con1 = view.findViewById(R.id.new_con1);
        new_con2 = view.findViewById(R.id.new_con2);

        img_con = view.findViewById(R.id.img_con);
        new_badge_con1 = view.findViewById(R.id.new_badge_con1);
        new_badge_con2 = view.findViewById(R.id.new_badge_con2);
        mode_switch_con = view.findViewById(R.id.mode_switch_con);

        img_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_profile.callOnClick();
            }
        });
        my_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_profile.callOnClick();
            }
        });
        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        my_child_profile_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                my_child_profile.callOnClick();
            }
        });
        my_child_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ChildProfileActivity.class);
                startActivity(intent);
            }
        });
        like_sitter_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like_sitter.callOnClick();
            }
        });
        like_sitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), LikeSitterActivity.class);
                startActivity(intent);
            }
        });
        notice_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notice.callOnClick();
            }
        });
        notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NoticeActivity.class);
                startActivity(intent);
            }
        });
        helper_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                helper.callOnClick();
            }
        });
        helper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), HelperActivity.class);
                startActivity(intent);
            }
        });
        one_to_one_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one_to_one.callOnClick();
            }
        });
        one_to_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ContactUsActivity.class);
                startActivity(intent);
            }
        });
        preferences_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preferences.callOnClick();
            }
        });
        preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PreferencesActivity.class);
                intent.putExtra("mode", "mom");
                intent.putExtra("support", support);
                startActivity(intent);
            }
        });
        about_service_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                about_service.callOnClick();
            }
        });
        about_service.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), TermsListActivity.class);
                startActivity(intent);
            }
        });
        refund_policy_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refund_policy.callOnClick();
            }
        });
        refund_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), RefundGuideActivity.class);
                startActivity(intent);
            }
        });

        sitter_support_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sitter_support.callOnClick();
            }
        });
        sitter_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("no".equals(support)) {
                    Intent intent = new Intent(getContext(), SupportSitterActivity.class);
                    startActivity(intent);
                } else if ("yes".equals(support)) {
                    oneBtnDialog = new OneBtnDialog(context, "이웃 친구를\n이미 지원하셨습니다 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                }
            }
        });

        mode_con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode_switch_icon.callOnClick();
            }
        });
        mode_switch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mode_switch_icon.callOnClick();
            }
        });
        mode_switch_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ("no".equals(mode)) {
                    oneBtnDialog = new OneBtnDialog(context, "이웃 친구를 지원해주세요 !", "확인");
                    oneBtnDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    oneBtnDialog.setCancelable(false);
                    oneBtnDialog.show();
                    return;
                } else if ("yes".equals(mode)) {
                    SharedPreferences prefLoginChecked = context.getSharedPreferences("modeChecked", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefLoginChecked.edit();
                    editor.remove("mode");
                    editor.putString("mode", "sitter");
                    editor.commit();
                    getActivity().finish();

                    Intent intent = new Intent(context, Main2Activity.class);
                    intent.putExtra("name", name.getText().toString());
                    startActivity(intent);
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
                viewSettings();
            }
        }, 500);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(prefToken.getString("tab", "").equals("5")){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case 9999:
                Intent LogoutIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(LogoutIntent);
                getActivity().finish();
                break;
        }
    }

    void viewSettings() {
        SharedPreferences get_token = context.getSharedPreferences("prefToken", Activity.MODE_PRIVATE);
        final String getToken = get_token.getString("Token", "");
        final String url = ServerUrl.getBaseUrl() + "/setting";
        Map<String, Object> params = new HashMap<String, Object>();
        aQuery.ajax(url, params, JSONObject.class, new AjaxCallback<JSONObject>() {
            @Override
            public void callback(String url, JSONObject jsonObject, AjaxStatus status) {
                Log.i(TAG, " " + jsonObject);
                try {
                    if (jsonObject.getBoolean("return")) {    //return이 true 면?
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        name.setText(jsonData.getString("name"));
                        if (!"null".equals(jsonData.getString("profile"))) {
                            circleImage(my_img, ServerUrl.getBaseUrl() + "/uploads/images/origin/" + jsonData.getString("profile"));
                        }
                        if ("null".equals(jsonData.getString("like")) || Integer.parseInt(jsonData.getString("like")) < 0) {
                            new_con1.setVisibility(View.GONE);
                        } else {
                            new_con1.setVisibility(View.VISIBLE);
                        }
                        if ("null".equals(jsonData.getString("friend"))) {
                            new_con2.setVisibility(View.GONE);
                        } else {
                            new_con2.setVisibility(View.VISIBLE);
                        }
                        if ("1".equals(jsonData.getString("level"))) {
                            //이웃 친구 모드 변경 안됨
                            mode = "no";
                        } else if ("2".equals(jsonData.getString("level"))) {
                            // 변경 가능
                            mode = "yes";
                        }
                        if ("0".equals(jsonData.getString("support"))) {
                            //지원 안한 상태 ( 지원 가능)
                            support = "no";
                        } else if ("1".equals(jsonData.getString("support"))) {
                            //지원한 상태 ( 지원 불가능)
                            support = "yes";
                        }
                    } else if (!jsonObject.getBoolean("return")) {
                        name.setText("정보 불러오기 실패");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.header("EPOCH-AGENT", "" + getToken).header("User-Agent", "android"));
    }

    private void circleImage(ImageView imageView, String getImg) {
        Log.i(TAG, " circleImage : " + getImg);
        Glide.with(context)
                .load(getImg)
                .apply(RequestOptions.bitmapTransform(new CropCircleTransformation()))
                .into(imageView);
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
