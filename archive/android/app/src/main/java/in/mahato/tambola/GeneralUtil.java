package in.mahato.tambola;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.accessibility.AccessibilityManager;

public class GeneralUtil {

    public static  boolean isFireTv(Context context){
        PackageManager pm = context.getPackageManager();
        if (pm.hasSystemFeature("amazon.hardware.fire_tv")){
            return true;
        }
        if( Build.MANUFACTURER.equalsIgnoreCase("Amazon")&& Build.MODEL.startsWith("AFT")){
                return true;
        }

        return false;
    }


}
