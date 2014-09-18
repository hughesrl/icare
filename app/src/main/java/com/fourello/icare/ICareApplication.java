package com.fourello.icare;

import android.app.Activity;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.fourello.icare.datas.ClinicSurvey;
import com.fourello.icare.datas.Doctors;
import com.fourello.icare.datas.MedsAndVaccines;
import com.fourello.icare.datas.Patients;
import com.fourello.icare.datas.Settings;
import com.fourello.icare.datas.SpinnerItems;
import com.fourello.icare.datas.Visits;
import com.fourello.icare.widgets.ParseProxyObject;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseObject;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@ReportsCrashes(
        formKey = "",
        formUri = "https://robehughes.iriscouch.com/acra-icare/_design/acra-storage/_update/report",
        reportType = org.acra.sender.HttpSender.Type.JSON,
        httpMethod = org.acra.sender.HttpSender.Method.PUT,
        formUriBasicAuthLogin="flipstop",
        formUriBasicAuthPassword="flipstop"
)
public class ICareApplication extends Application {
    public static String PARENT_GROUP = "ParentGroup";
    public static String DOCTORS_GROUP = "doctorGroup";
    public static String PATIENTS_GROUP = "patientsGroup";

    public static String DOCTORS_LABEL = "Doctors";
    public static String USERS_LABEL = "Users";
    public static String PATIENTS_LABEL = "Patients";
    public static String VISITS_LABEL = "Visits";
    public static String CLINIC_SURVEY_LABEL = "ClinicSurvey";
    public static String PROMO_LABEL = "Promos";
    public static String SETTINGS_LABEL = "Settings";

    public static ParseProxyObject mLoginData;

    @Override
    public void onCreate() {
        super.onCreate();

        // ACRA
        ACRA.init(this);

        // Add your initialization code here
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Patients.class);
        ParseObject.registerSubclass(Visits.class);
        ParseObject.registerSubclass(MedsAndVaccines.class);
        ParseObject.registerSubclass(Doctors.class);
//        ParseObject.registerSubclass(PatientCheckIn.class);
        ParseObject.registerSubclass(ClinicSurvey.class);
        ParseObject.registerSubclass(Settings.class);

        Parse.initialize(this, "mgjwXn2NpSfBlZenRbnWGYFqIOfZ0AdGUIqXxq9k", "RRw4cxwZG4wZryUuxqYHbzYLtof8fvizS7JhQ6PO"); // Robert
//        Parse.initialize(this, "85lVaxRqcrhlyzzWa3QckqBpP7GJiKolwT16MJnk", "Nz1kPiFcgMm6SadNGPXPgxV4RRpOKQVUwsDC6pEc"); // Icare Fourello

        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();

        // If you would like all objects to be private by default, remove this line.
        defaultACL.setPublicReadAccess(true);
        defaultACL.setPublicWriteAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);

    }

    /**
     * method is used for checking valid email id format.
     *
     * @param email
     * @return boolean true for valid false for invalid
     */
    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static Bitmap takeScreenShot(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;
        int width = activity.getWindowManager().getDefaultDisplay().getWidth();
        int height = activity.getWindowManager().getDefaultDisplay().getHeight();

        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height  - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }
    public static Bitmap fastblur(Bitmap sentBitmap, int radius) {
        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = ( 0xff000000 & pix[yi] ) | ( dv[rsum] << 16 ) | ( dv[gsum] << 8 ) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        Log.e("pix", w + " " + h + " " + pix.length);
        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }


    public static ArrayList<SpinnerItems> populateRelationshipToPatient(){
        final ArrayList<SpinnerItems> spinnerItems = new ArrayList<SpinnerItems>();
        spinnerItems.add(new SpinnerItems("", false));
        spinnerItems.add(new SpinnerItems("Father", true));
        spinnerItems.add(new SpinnerItems("Mother", true));
        spinnerItems.add(new SpinnerItems("Relative", true));
        return spinnerItems;
    }
    public static ArrayList<SpinnerItems> populatePurpose(){
        final ArrayList<SpinnerItems> spinnerItems = new ArrayList<SpinnerItems>();
        spinnerItems.add(new SpinnerItems("", false));
        spinnerItems.add(new SpinnerItems("Checkup", true));
        spinnerItems.add(new SpinnerItems("Follow-up", true));
        spinnerItems.add(new SpinnerItems("Vaccination", true));
        return spinnerItems;
    }
    public static ArrayList<SpinnerItems> populateTypeOfDelivery(){
        final ArrayList<SpinnerItems> spinnerItems = new ArrayList<SpinnerItems>();
        spinnerItems.add(new SpinnerItems("", false));
        spinnerItems.add(new SpinnerItems("Normal", true));
        spinnerItems.add(new SpinnerItems("CS", true));
        return spinnerItems;
    }
    public static ArrayList<SpinnerItems> populateAllergyRisk(){
        final ArrayList<SpinnerItems> spinnerItems = new ArrayList<SpinnerItems>();
        spinnerItems.add(new SpinnerItems("", false));
        spinnerItems.add(new SpinnerItems("At Risk", true));
        spinnerItems.add(new SpinnerItems("Medium Risk (15%-40%)", true));
        spinnerItems.add(new SpinnerItems("High Risk (50%-80%)", true));
        return spinnerItems;
    }

    public static ArrayList<SpinnerItems> populateGender(){
        final ArrayList<SpinnerItems> spinnerItems = new ArrayList<SpinnerItems>();
        spinnerItems.add(new SpinnerItems("", false));
        spinnerItems.add(new SpinnerItems("Male", true));
        spinnerItems.add(new SpinnerItems("Female", true));
        return spinnerItems;
    }
}
