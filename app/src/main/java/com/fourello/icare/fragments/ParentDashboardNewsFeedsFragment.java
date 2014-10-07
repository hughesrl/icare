package com.fourello.icare.fragments;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.fourello.icare.BTGridPager.BTFragmentGridPager;
import com.fourello.icare.DashboardParentFragmentActivity;
import com.fourello.icare.R;
import com.fourello.icare.adapters.ParentDashboardNewsFeedsListAdapter;
import com.fourello.icare.datas.NewsFeeds;
import com.fourello.icare.datas.PatientChildData;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.widgets.ParseProxyObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ParentDashboardNewsFeedsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ParentDashboardNewsFeedsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ParentDashboardNewsFeedsFragment extends Fragment {
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
    private ListView newsFeedsListView;
    private ParseQueryAdapter.QueryFactory<NewsFeeds> queryFactory;
    private boolean isConnected;
    private ParentDashboardNewsFeedsListAdapter dashboardNewsFeedsListAdapter;
    private ProgressBar loadingBar;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyDashboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ParentDashboardNewsFeedsFragment newInstance(String param1, String param2) {
        ParentDashboardNewsFeedsFragment fragment = new ParentDashboardNewsFeedsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public ParentDashboardNewsFeedsFragment() {
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
        myFragmentView = (ViewGroup) inflater.inflate(R.layout.fragment_parent_dashboard_news_feeds, container, false);

        newsFeedsListView = (ListView) myFragmentView.findViewById(R.id.listNewsFeeds);

        loadingBar = (ProgressBar) myFragmentView.findViewById(R.id.loadingBar);

        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        new PopulateNewsFeedsDataTask().execute();
//        if (!isConnected) {

//        } else {
//            queryFactory = new ParseQueryAdapter.QueryFactory<NewsFeeds>() {
//                public ParseQuery<NewsFeeds> create() {
//                    ParseQuery<NewsFeeds> query = NewsFeeds.getQuery();
//                    query.addDescendingOrder("createdAt");
//                    return query;
//                }
//            };
//        }



//            symptomsTracker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                }
//            });


        return myFragmentView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//    }

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

    public class PopulateNewsFeedsDataTask extends AsyncTask<Void, Void, Void> {
        // Override this method to do custom remote calls
        protected Void doInBackground(Void... params) {
            queryFactory = new ParseQueryAdapter.QueryFactory<NewsFeeds>() {
                public ParseQuery<NewsFeeds> create() {
                    ParseQuery<NewsFeeds> query = NewsFeeds.getQuery();
                    query.addDescendingOrder("createdAt");
                    query.fromLocalDatastore();
                    return query;
                }
            };

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadingBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void result) {
            dashboardNewsFeedsListAdapter = new ParentDashboardNewsFeedsListAdapter(getActivity(), queryFactory);
            // Attach the query adapter to the view
            newsFeedsListView.setAdapter(dashboardNewsFeedsListAdapter);
            newsFeedsListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            break;

                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }

                    // Handle ListView touch events.
                    v.onTouchEvent(event);
                    return true;
                }
            });
            newsFeedsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    NewsFeeds newsFeeds = dashboardNewsFeedsListAdapter.getItem(position);
                    showDialogNewsFeed(newsFeeds.getTitle(), newsFeeds.getUrl());
                }
            });
            loadingBar.setVisibility(View.GONE);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showDialogNewsFeed(String title, String url) {
        final Dialog dialog = new Dialog(getActivity(), R.style.DialogSlideAnimZoom);

        dialog.setContentView(R.layout.dialog_dashboard_news_feed);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

//        loadingBar = (ProgressBar) dialog.findViewById(R.id.loadingBar);
//
//        loadingBar.setVisibility(View.VISIBLE);
        final LinearLayout loadingBarLayout = (LinearLayout)dialog.findViewById(R.id.loadingBarLayout);

        CustomTextView pageTitle = (CustomTextView)dialog.findViewById(R.id.pageTitle);
        pageTitle.setText(title);

        final WebView webNewsFeed = (WebView)dialog.findViewById(R.id.webNewsFeed);

        webNewsFeed.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                loadingBarLayout.setVisibility(View.GONE);
                webNewsFeed.setVisibility(View.VISIBLE);
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            }
        });
        webNewsFeed.loadUrl(url);

        ImageButton btnCloseMenuMedicine = (ImageButton) dialog.findViewById(R.id.btnCloseMenuMedicine);
        btnCloseMenuMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }
}
