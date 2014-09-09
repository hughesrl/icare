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


public class ParentDashboardFragment extends Fragment implements
        DashboardParentFragmentActivity.OpenMenuCallbacks {
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
    private byte[] mParamMyPicture;



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
        args.putString(ARG_LOGIN_DATA, param1);
        args.putString(ARG_MY_PICTURE, param2);
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
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(ARG_LOGIN_DATA);
            mParamChildData = getArguments().getParcelableArrayList(ARG_CHILD_DATA);
            mParamChildDataPosition = getArguments().getInt(ARG_CHILD_DATA_POS);
            mParamMyPicture = getArguments().getByteArray(ARG_MY_PICTURE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard_parent, container, false);

        Toast.makeText(getActivity(), "POS "+mParamChildDataPosition, Toast.LENGTH_LONG).show();
        ((DashboardParentFragmentActivity)getActivity()).changePageTitle(mParamChildData.get(mParamChildDataPosition).getPatientName());
//        // The content view embeds two fragments; now retrieve them and attach
//        // their "hide" button.
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//
//        int accessType = Integer.parseInt(mParamLoginData.getString("type"));
//        switch (accessType) {
//            case 1 : // Doctor
//
//                break;
//            case 2 : // Secretary
//                int height = getResources().getDisplayMetrics().heightPixels - 120;
//                int patientsHeight = height - (height/4); // 3/4
//                int promosHeight = height - patientsHeight; // 1/4
//
//                hideFragmentListener(fm.findFragmentById(R.id.my_dashboard_frag));
//                hideFragmentListener(fm.findFragmentById(R.id.scheduler_frag));
//                hideFragmentListener(fm.findFragmentById(R.id.news_feed_frag));
//
//                /* PATIENT QUEUE */
//                FrameLayout frame = (FrameLayout) myFragmentView.findViewById(R.id.patient_queue_frag);
//                ViewGroup.LayoutParams patientsQueueParams = frame.getLayoutParams();
//                patientsQueueParams.height = patientsHeight;
//                frame.setLayoutParams(patientsQueueParams);
//
//                PatientQueueFragment patientQueueFragment = new PatientQueueFragment();
//                Bundle bundle = new Bundle();
//                bundle.putSerializable(PatientQueueFragment.ARG_LOGIN_DATA, mParamLoginData);
//                bundle.putString(PatientQueueFragment.ARG_DOCTOR_ID, mParamLoginData.getString("linked_doctorid"));
//                patientQueueFragment.setArguments(bundle);
//                ft.replace(R.id.patient_queue_frag, patientQueueFragment, "PATIENTS_QUEUE_FRAGMENT");
//                ft.commit();
//
//
//                Fragment promosFrag = fm.findFragmentById(R.id.promos_frag);
//                ViewGroup.LayoutParams promosParams = promosFrag.getView().getLayoutParams();
//                promosParams.height = promosHeight;
//                promosFrag.getView().setLayoutParams(promosParams);
//
//                break;
//
//            default:
//                break;
//        }
        return myFragmentView;
    }

    @Override
    public void onMenuPressedCallback() {
        ((DashboardParentFragmentActivity) getActivity()).showMenuContents(getView());
    }
}
