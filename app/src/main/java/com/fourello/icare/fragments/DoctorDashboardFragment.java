package com.fourello.icare.fragments;

import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fourello.icare.DashboardDoctorFragmentActivity;
import com.fourello.icare.R;

import com.fourello.icare.widgets.ParseProxyObject;
import com.fourello.icare.widgets.PasswordDialogFragment;


public class DoctorDashboardFragment extends Fragment implements
        DashboardDoctorFragmentActivity.OpenMenuCallbacks {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_LOGIN_DATA = "loginData";
    private static final String ARG_MY_PICTURE = "myPicture";

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private byte[] mParamMyPicture;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SchedulerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DoctorDashboardFragment newInstance(String param1, String param2) {
        DoctorDashboardFragment fragment = new DoctorDashboardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN_DATA, param1);
        args.putString(ARG_MY_PICTURE, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public DoctorDashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParamLoginData = (ParseProxyObject) getArguments().getSerializable(ARG_LOGIN_DATA);
            mParamMyPicture = getArguments().getByteArray(ARG_MY_PICTURE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard, container, false);

        ((DashboardDoctorFragmentActivity)getActivity()).changePageTitle(mParamLoginData.getString("firstname")+" "+mParamLoginData.getString("lastname"));
        // The content view embeds two fragments; now retrieve them and attach
        // their "hide" button.
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        int accessType = Integer.parseInt(mParamLoginData.getString("type"));
        switch (accessType) {
            case 1 : // Doctor

                break;
            case 2 : // Secretary
                int height = getResources().getDisplayMetrics().heightPixels - 120;
                int patientsHeight = height - (height/4); // 3/4
                int promosHeight = height - patientsHeight; // 1/4

                hideFragmentListener(fm.findFragmentById(R.id.my_dashboard_frag));
                hideFragmentListener(fm.findFragmentById(R.id.scheduler_frag));
                hideFragmentListener(fm.findFragmentById(R.id.news_feed_frag));

                Fragment patientsQueueFrag =  fm.findFragmentById(R.id.patient_queue_frag);
                ViewGroup.LayoutParams patientsQueueParams = patientsQueueFrag.getView().getLayoutParams();
                patientsQueueParams.height = patientsHeight;
                patientsQueueFrag.getView().setLayoutParams(patientsQueueParams);

                Fragment promosFrag = fm.findFragmentById(R.id.promos_frag);
                ViewGroup.LayoutParams promosParams = promosFrag.getView().getLayoutParams();
                promosParams.height = promosHeight;
                promosFrag.getView().setLayoutParams(promosParams);

                break;

            default:
                break;
        }
        return myFragmentView;
    }

    @Override
    public void onMenuPressedCallback() {
        ((DashboardDoctorFragmentActivity) getActivity()).showMenuContents(getView());
    }

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }

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
        public void onFragmentInteraction(Uri uri);
    }

    void hideFragmentListener(Fragment fragment) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(fragment);
        ft.commit();
    }
}
