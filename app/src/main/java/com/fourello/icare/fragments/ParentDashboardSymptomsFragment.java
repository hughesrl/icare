package com.fourello.icare.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fourello.icare.BTGridPager.BTFragmentGridPager;
import com.fourello.icare.DashboardParentFragmentActivity;
import com.fourello.icare.R;
import com.fourello.icare.datas.PatientChildData;
import com.fourello.icare.datas.PatientNotes;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.ParseProxyObject;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.json.JSONArray;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link com.fourello.icare.fragments.ParentDashboardSymptomsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link com.fourello.icare.fragments.ParentDashboardSymptomsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ParentDashboardSymptomsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private ParseProxyObject mParamLoginData;
    private ArrayList<PatientChildData> mParamChildData;
    private int mParamChildDataPosition;
    private byte[] mParamMyPicture;
    private String mParam1;
    private String mParam2;

    private ViewGroup myFragmentView;
    private OnFragmentInteractionListener mListener;

    private BTFragmentGridPager.FragmentGridPagerAdapter mFragmentGridPagerAdapter;
    private JSONArray arrayMedications;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyDashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParentDashboardSymptomsFragment newInstance(String param1, String param2) {
        ParentDashboardSymptomsFragment fragment = new ParentDashboardSymptomsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ParentDashboardSymptomsFragment() {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_parent_dashboard_symptoms, container, false);

        final CustomTextView txtDateSymptom = (CustomTextView) myFragmentView.findViewById(R.id.txtDateSymptom);
        final CustomTextView txtSubject = (CustomTextView) myFragmentView.findViewById(R.id.txtSubject);
        final CustomTextView txtSymptoms = (CustomTextView) myFragmentView.findViewById(R.id.txtSymptoms);

        ParseQuery<PatientNotes> querySymptoms = PatientNotes.getQuery();
        querySymptoms.fromLocalDatastore();
        querySymptoms.setSkip(0);
        querySymptoms.addDescendingOrder("updatedAt");
        try {
            if(querySymptoms.count() == 0) {
                LinearLayout nodata = (LinearLayout) myFragmentView.findViewById(R.id.nodata);
                nodata.setVisibility(View.VISIBLE);

                LinearLayout content = (LinearLayout) myFragmentView.findViewById(R.id.content);
                content.setVisibility(View.GONE);
            } else {
                querySymptoms.getFirstInBackground(new GetCallback<PatientNotes>() {
                        @Override
                        public void done(PatientNotes parseObject, ParseException e) {
                            if(e == null) {
                                DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");

//                                int remainingDays = getDaysDifference(new Date(), parseObject.getUpdatedAt());

//                                if(remainingDays > 0) {
                                    txtDateSymptom.setText(Html.fromHtml("<u>" + df.format(parseObject.getUpdatedAt()) + "<u>"));
                                    if (parseObject.containsKey("subject")) {
                                        txtSubject.setText(parseObject.getSubject());
                                    }
                                    if (parseObject.containsKey("notes")) {
                                        txtSymptoms.setText(parseObject.getNotes());
                                    }
//                                } else {
//                                    LinearLayout nodata = (LinearLayout) myFragmentView.findViewById(R.id.nodata);
//                                    nodata.setVisibility(View.VISIBLE);
//
//                                    LinearLayout content = (LinearLayout) myFragmentView.findViewById(R.id.content);
//                                    content.setVisibility(View.GONE);
//                                }
                            }
                        }
                    });
                }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return myFragmentView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        public void onFragmentInteraction(Uri uri);
    }


    public static int getDaysDifference(Date fromDate, Date toDate) {
        if(fromDate==null||toDate==null)
            return 0;

        return (int)( (toDate.getTime() - fromDate.getTime()) / (1000 * 60 * 60 * 24));
    }
}
