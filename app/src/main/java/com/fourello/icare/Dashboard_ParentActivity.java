package com.fourello.icare;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.fourello.icare.view.RoundedImageView;
import com.parse.ParseFile;


public class Dashboard_ParentActivity extends Activity {

    public Dialog dialog;
    public ImageButton btnShowMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_dashboard);

        btnShowMenu = (ImageButton)findViewById(R.id.btnShowMenu);
        dialog = new Dialog(Dashboard_ParentActivity.this, R.style.DialogSlideAnim);
        dialog.setContentView(R.layout.activity_menu);

        showMenu(btnShowMenu);




    }

    @Override
    public void onStart() {
        super.onStart();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(dialog.isShowing()) {
            dialog.dismiss();
            btnShowMenu.setVisibility(View.VISIBLE);
        } else {
            this.finish();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showMenu(final View view) {
        view.setVisibility(View.VISIBLE);
        if(dialog.isShowing()) {
            dialog.dismiss();
        } else {
            dialog.setContentView(R.layout.activity_menu);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    dialog.show();
                    view.setVisibility(View.INVISIBLE);

                    ImageButton btnCloseMenu = (ImageButton)dialog.findViewById(R.id.btnCloseMenu);
                    btnCloseMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            view.setVisibility(View.VISIBLE);
                        }
                    });



                    runOnUiThread(new Runnable() {
                        public void run() {
//                            ParseFile imageFile = (ParseFile)parseObject.get(Statics.COL_IMAGE_FILE);
//                            imageFile.getDataInBackground(new GetDataCallback() {
//                                public void done(byte[] data, ParseException e) {
//                                    if (e == null) {
//                                        // data has the bytes for the image
//                                    } else {
//                                        // something went wrong
//                                    }
//                                }
//                            });
                            Bitmap myProfilePic = BitmapFactory.decodeResource(getResources(), R.drawable.doctor_icon);
                            ImageView profilPic = (ImageView) dialog.findViewById(R.id.btnMyPicture);
                            Bitmap profileInCircle = RoundedImageView.getRoundedCornerBitmap(myProfilePic);
                            profilPic.setImageBitmap(profileInCircle);
                        }
                    });
                }
            }, 100);

        }
    }
}
