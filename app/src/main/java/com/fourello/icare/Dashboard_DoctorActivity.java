//package com.fourello.icare;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.FragmentTransaction;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.util.Log;
//import android.view.Menu;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//import com.fourello.icare.adapters.MenuItemsAdapter;
//import com.fourello.icare.datas.MenuItems;
//import com.fourello.icare.view.CustomTextView;
//import com.fourello.icare.view.RoundedImageView;
//import com.fourello.icare.widgets.ParseProxyObject;
//import com.parse.DeleteCallback;
//import com.parse.Parse;
//import com.parse.ParseException;
//import com.parse.ParseFile;
//import com.parse.ParseObject;
//import com.parse.ParseQuery;
//
//import java.io.Serializable;
//import java.util.HashMap;
//
//public class Dashboard_DoctorActivity extends Activity {
//
//    public Dialog dialog;
//    public ImageButton btnShowMenu;
//    public ParseProxyObject loginData;
//
//    public byte[] myPicture;
//
//    public ListView listSubMenu;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
//        getActionBar().hide();
//        setContentView(R.layout.activity_dashboard);
//
//        Intent extras = getIntent();
//        loginData = (ParseProxyObject) extras.getSerializableExtra("loginData");
//
//        myPicture = extras.getByteArrayExtra("myPicture");
//
//        // The content view embeds two fragments; now retrieve them and attach
//        // their "hide" button.
//        FragmentManager fm = getFragmentManager();
//
//
//        int accessType = Integer.parseInt(loginData.getString("type"));
//        switch (accessType) {
//            case 1 :
//
//                break;
//            case 2 :
//                int height = getResources().getDisplayMetrics().heightPixels - 120;
//                int patientsHeight = height - (height/4); // 3/4
//                int promosHeight = height - patientsHeight; // 1/4
//
//                hideFragmentListener(fm.findFragmentById(R.id.my_dashboard_frag));
//                hideFragmentListener(fm.findFragmentById(R.id.scheduler_frag));
//                hideFragmentListener(fm.findFragmentById(R.id.news_feed_frag));
//
//                Fragment patientsQueueFrag = fm.findFragmentById(R.id.patient_queue_frag);
//                ViewGroup.LayoutParams patientsQueueParams = patientsQueueFrag.getView().getLayoutParams();
//                patientsQueueParams.height = patientsHeight;
//                patientsQueueFrag.getView().setLayoutParams(patientsQueueParams);
//
//                Fragment promosFrag = fm.findFragmentById(R.id.promos_frag);
//                ViewGroup.LayoutParams promosParams = promosFrag.getView().getLayoutParams();
//                promosParams.height = promosHeight;
//                promosFrag.getView().setLayoutParams(promosParams);
//
//
//
////                Toast.makeText(getApplicationContext(), "Patients:"+patientsHeight+" Promos:"+promosHeight,Toast.LENGTH_LONG);
////                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) patients_queue.getLayoutParams();
//////                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mainLayout.getLayoutParams();
////                params.height = patientsHeight;
////                patients_queue.setLayoutParams(params);
////                // -------------------------------
////                FrameLayout.LayoutParams params2 = (FrameLayout.LayoutParams) promos.getLayoutParams();
//////                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) mainLayout.getLayoutParams();
////                params2.height = promosHeight;
////                promos.setLayoutParams(params2);
//
//                break;
//
//            default:
//                break;
//        }
//
////        ImageView imgViewMyPictureTest = (ImageView) findViewById(R.id.profilePic);
////        Bitmap bMap = BitmapFactory.decodeByteArray(myPicture, 0, myPicture.length);
////        imgViewMyPictureTest.setImageBitmap(bMap);
//
////        Log.d("Robert Hughes", loginData.getValues().toString());
////        Log.d("Robert Hughes", "file: "+myPicture.length);
//
//        btnShowMenu = (ImageButton)findViewById(R.id.btnShowMenu);
//        dialog = new Dialog(Dashboard_DoctorActivity.this, R.style.DialogSlideAnim);
//        dialog.setContentView(R.layout.activity_menu);
//
//
//        showMenu(btnShowMenu);
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
//    }
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if(dialog.isShowing()) {
//            dialog.dismiss();
//            btnShowMenu.setVisibility(View.VISIBLE);
//        } else {
//            this.finish();
//        }
//    }
//
//
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
////    @Override
////    public boolean onOptionsItemSelected(MenuItem item) {
////        // Handle action bar item clicks here. The action bar will
////        // automatically handle clicks on the Home/Up button, so long
////        // as you specify a parent activity in AndroidManifest.xml.
////        int id = item.getItemId();
////        if (id == R.id.action_settings) {
////            return true;
////        }
////        return super.onOptionsItemSelected(item);
////    }
//
//    public void showMenu(final View view) {
//        view.setVisibility(View.VISIBLE);
//        if(dialog.isShowing()) {
//            dialog.dismiss();
//        } else {
//            dialog.setContentView(R.layout.activity_menu);
//
//            CustomTextView txtUserName = (CustomTextView) dialog.findViewById(R.id.userName);
//            txtUserName.setText(loginData.getString("firstname")+" "+loginData.getString("lastname"));
//
//            ImageView imgViewMyPicture = (ImageView) dialog.findViewById(R.id.btnMyPicture);
//            Bitmap bMap = BitmapFactory.decodeByteArray(myPicture, 0, myPicture.length);
//            Bitmap profileInCircle = RoundedImageView.getRoundedCornerBitmap(bMap);
//            imgViewMyPicture.setImageBitmap(profileInCircle);
//
//            // Get ListView object from xml
//            listSubMenu = (ListView) dialog.findViewById(R.id.listSubMenu);
//
//            ImageButton btnMyPatients = (ImageButton) dialog.findViewById(R.id.btnMyPatients);
//            btnMyPatients.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Defined Array values to show in ListView
//                    MenuItems menu_items_data[] = new MenuItems[] {
//                            new MenuItems("Add User", true),
//                            new MenuItems("Add Patient", true),
//                            new MenuItems("Patient Database", true),
//                            new MenuItems("Messages", false)
//                    };
//                    MenuItemsAdapter adapter = new MenuItemsAdapter(Dashboard_DoctorActivity.this, R.layout.list_menu_items, menu_items_data);
//                    // Assign adapter to ListView
//                    listSubMenu.setAdapter(adapter);
//                    listSubMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            dialog.dismiss();
//                            switch(position) {
//                                case 0 :
//                                    Intent intent = new Intent(Dashboard_DoctorActivity.this, AddUserActivity.class);
//                                    startActivity(intent);
//                                    finish();
//                                    break;
//                                case 1 :
//                                    break;
//                            }
//                        }
//                    });
//                }
//            });
//
//            ImageButton btnSettings = (ImageButton) dialog.findViewById(R.id.btnSettings);
//            btnSettings.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    // Defined Array values to show in ListView
//                    MenuItems menu_items_data[] = new MenuItems[] {
//                                    new MenuItems("Settings", true),
//                                    new MenuItems("Doctor Information", true),
//                                    new MenuItems("Logout", true)
//                            };
//                    final MenuItemsAdapter adapter = new MenuItemsAdapter(Dashboard_DoctorActivity.this, R.layout.list_menu_items, menu_items_data);
//                    // Assign adapter to ListView
//                    listSubMenu.setAdapter(adapter);
//
//                    listSubMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            switch (position) {
//                                case 0 :
//                                    break;
//                                case 1 :
//                                    break;
//                                case 2 :
//                                    ParseObject.unpinAllInBackground(new DeleteCallback() {
//                                        public void done(ParseException e) {
//                                            if (e != null) {
//                                                // There was some error.
//                                                return;
//                                            } else {
//                                                dialog.dismiss();
//                                                Intent intent = new Intent(Dashboard_DoctorActivity.this, LoginSignupActivity.class);
//                                                startActivity(intent);
//                                                finish();
//                                            }
//                                        }
//                                    });
//                                    break;
//                                default:
//                                    break;
//                            }
//                        }
//                    });
//                }
//            });
//
//            Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
////                    Bitmap map= ICareApplication.takeScreenShot(Dashboard_DoctorActivity.this);
////
////                    Bitmap fast=ICareApplication.fastblur(map, 10);
////                    final Drawable draw=new BitmapDrawable(getResources(),fast);
////                    dialog.getWindow().setBackgroundDrawable(draw);
//                    WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
//                    lp.dimAmount=0.9f;  // this sets the amount of darkening
//                    dialog.getWindow().setAttributes(lp);
//                    dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//
//                    dialog.show();
//                    view.setVisibility(View.INVISIBLE);
//
//                    ImageButton btnCloseMenu = (ImageButton)dialog.findViewById(R.id.btnCloseMenu);
//                    btnCloseMenu.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            dialog.dismiss();
//                            view.setVisibility(View.VISIBLE);
//                        }
//                    });
//                }
//            }, 10);
//
//        }
//    }
//
//    void addShowHideListener(boolean showFragment, Fragment fragment) {
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        if(showFragment == false) {
//            ft.hide(fragment);
//        } else {
//            ft.show(fragment);
//        }
//        ft.commit();
//    }
//    void hideFragmentListener(Fragment fragment) {
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.hide(fragment);
//        ft.commit();
//    }
//}
