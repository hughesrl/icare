package com.fourello.icare.widgets;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.fourello.icare.R;
import com.fourello.icare.view.CustomTextView;
import com.fourello.icare.view.RoundedAvatarDrawable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Utils {
    public static void validatePassword(Context context) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);

        alert.setTitle("Title");
        alert.setMessage("Message");

        // Set an EditText view to get user input
        final EditText input = new EditText(context);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
//                String value = input.getText();
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progressdialog);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();

        windowParams.dimAmount = 0.90f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
        // dialog.setMessage(Message);
        return dialog;
    }
    public static ProgressDialog createProgressDialogWithMessage(Context mContext, String msg) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.progressdialog);

        CustomTextView lblLoadingMessage = (CustomTextView) dialog.findViewById(R.id.lblLoadingMessage);
        lblLoadingMessage.setVisibility(View.VISIBLE);

        lblLoadingMessage.setText(msg);

        Window window = dialog.getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();

        windowParams.dimAmount = 0.90f;
        windowParams.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(windowParams);
        // dialog.setMessage(Message);
        return dialog;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    public static RoundedAvatarDrawable resizedBitmapDisplayUserPhoto(Activity activity, Bitmap bitmap) {
        int width = activity.getResources().getInteger(R.integer.resize_user_photo_size_size);
        int height = activity.getResources().getInteger(R.integer.resize_user_photo_size_size);
        Bitmap resizedbitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        RoundedAvatarDrawable r = new RoundedAvatarDrawable(resizedbitmap);

        return r;
    }

    public static RoundedAvatarDrawable resizedBitmapDisplayPatientQueue(Activity activity, Bitmap bitmap) {
        int width = activity.getResources().getInteger(R.integer.resize_patient_queue_photo_size);
        int height = activity.getResources().getInteger(R.integer.resize_patient_queue_photo_size);
        Bitmap resizedbitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        RoundedAvatarDrawable r = new RoundedAvatarDrawable(resizedbitmap);

        return r;
    }

    public static RoundedAvatarDrawable resizedBitmapDisplay(Activity activity, Bitmap bitmap) {
        int width = activity.getResources().getInteger(R.integer.resize_patient_photo_size);
        int height = activity.getResources().getInteger(R.integer.resize_patient_photo_size);
        Bitmap resizedbitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
        RoundedAvatarDrawable r = new RoundedAvatarDrawable(resizedbitmap);

        return r;
    }

    private static final String ALLOWED_CHARACTERS ="0123456789QWERTYUIOPASDFGHJKLZXCVBNM";

    public static String getRandomString(final int sizeOfRandomString, final ArrayList<String> allRandomPINS) {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder();
        final Boolean noSamePIN = false;
        for(int i=0;i<sizeOfRandomString;++i) {
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
            if((sizeOfRandomString-1) == i) {
                if(allRandomPINS.contains(sb.toString())) {
                    // Exists
                    i = 0;
                    sb.delete(0, sizeOfRandomString-1);
                } else {
                    // Nothing to do. continue
                }
            }
        }
        return sb.toString();
    }
}
