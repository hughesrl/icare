package com.fourello.icare.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fourello.icare.DashboardParentFragmentActivity;
import com.fourello.icare.R;
import com.fourello.icare.datas.PatientChildData;
import com.fourello.icare.widgets.ParseProxyObject;

import java.util.ArrayList;


public class ParentDashboardFragment extends Fragment implements
        DashboardParentFragmentActivity.OpenMenuCallbacks {
    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    public static final String ARG_LOGIN_DATA = "loginData";
//    public static final String ARG_CHILD_DATA = "childData";
//    public static final String ARG_CHILD_DATA_POS = "childDataPosition";
//    public static final String ARG_MY_PICTURE = "myPicture";

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private ArrayList<PatientChildData> mParamChildData;
    private int mParamChildDataPosition;
    private byte[] mParamMyPicture;
    private ProgressDialog mProgressDialog;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParentDashboardFragment newInstance(String param1, String param2) {
        ParentDashboardFragment fragment = new ParentDashboardFragment();
        Bundle args = new Bundle();
        args.putString(DashboardParentFragmentActivity.ARG_LOGIN_DATA, param1);
        args.putString(DashboardParentFragmentActivity.ARG_MY_PICTURE, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ParentDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(DashboardParentFragmentActivity.ARG_LOGIN_DATA);
            mParamChildData = getArguments().getParcelableArrayList(DashboardParentFragmentActivity.ARG_CHILD_DATA);
            mParamChildDataPosition = getArguments().getInt(DashboardParentFragmentActivity.ARG_CHILD_DATA_POS);
            mParamMyPicture = getArguments().getByteArray(DashboardParentFragmentActivity.ARG_MY_PICTURE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_parent_dashboard, container, false);

        ((DashboardParentFragmentActivity)getActivity()).changePageTitle(mParamChildData.get(mParamChildDataPosition).getFirtname()+"'s snapshot");

        // The content view embeds two fragments; now retrieve them and attach
        // their "hide" button.
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        Bundle bundle = getArguments();

        ParentMyBabyFragment myBabyFragment = new ParentMyBabyFragment();
        myBabyFragment.setArguments(bundle);
        ft.replace(R.id.my_baby, myBabyFragment, "MY_BABY_FRAGMENT");
        //ft.commit();

        ParentPreviousVisitFragment previousVisitFragment = new ParentPreviousVisitFragment();
        previousVisitFragment.setArguments(bundle);
        ft.replace(R.id.previous_visits, previousVisitFragment, "PREVIOUS_VISIT_FRAGMENT");

        ft.commit();



        return myFragmentView;
    }

    @Override
    public void onMenuPressedCallback() {
        ((DashboardParentFragmentActivity) getActivity()).showMenuContents(getView());
    }
}
