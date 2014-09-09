package com.fourello.icare.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fourello.icare.DashboardParentFragmentActivity;
import com.fourello.icare.R;
import com.fourello.icare.datas.MyChildren;
import com.fourello.icare.widgets.ParseProxyObject;

import java.util.ArrayList;

public class PromosFragmentTest extends Fragment implements DashboardParentFragmentActivity.OpenMenuCallbacks{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LOGIN_DATA = "loginData";
    public static final String ARG_CHILD_DATA = "childData";
    public static final String ARG_CHILD_DATA_POS = "childDataPosition";
    private static final String ARG_MY_PICTURE = "myPicture";

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private ArrayList<MyChildren> mParamChildData;
    private int mParamChildDataPosition;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PromosFragmentTest newInstance(String param1, String param2) {
        PromosFragmentTest fragment = new PromosFragmentTest();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }
    public PromosFragmentTest() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(ARG_LOGIN_DATA);
            mParamChildData = getArguments().getParcelableArrayList(ARG_CHILD_DATA);
            mParamChildDataPosition = getArguments().getInt(ARG_CHILD_DATA_POS);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_promos, container, false);
        Toast.makeText(getActivity(), "POS " + mParamChildDataPosition, Toast.LENGTH_LONG).show();
        ((DashboardParentFragmentActivity)getActivity()).changePageTitle(mParamChildData.get(mParamChildDataPosition).getPatientName());

        return myFragmentView;
    }


    @Override
    public void onMenuPressedCallback() {
        ((DashboardParentFragmentActivity) getActivity()).showMenuContents(getView());
    }
}
