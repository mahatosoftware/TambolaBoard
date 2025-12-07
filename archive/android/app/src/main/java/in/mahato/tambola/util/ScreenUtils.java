package in.mahato.tambola.util;

import android.app.Activity;
import android.util.DisplayMetrics;

public class ScreenUtils {

    public static int getScreenWidthPx(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeightPx(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int getScreenWidthDp(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        return Math.round(dm.widthPixels / dm.density);
    }

    public static int getScreenHeightDp(Activity activity) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        return Math.round(dm.heightPixels / dm.density);
    }
}
