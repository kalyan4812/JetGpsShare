package com.saikalyandaroju.jetgpsshare.Utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.intuit.sdp.BuildConfig;

/**
 * Created by karandeep on 09-May-17.
 */

public class ChinesePermissionUtils {

    /**
     * Build.MANUFACTURER
     */
    private static final String MANUFACTURER_HUAWEI = "Huawei";
    private static final String MANUFACTURER_MEIZU = "Meizu";
    private static final String MANUFACTURER_XIAOMI = "Xiaomi";
    private static final String MANUFACTURER_SONY = "Sony";
    private static final String MANUFACTURER_OPPO = "OPPO";
    private static final String MANUFACTURER_LG = "LG";
    private static final String MANUFACTURER_VIVO = "vivo";
    private static final String MANUFACTURER_SAMSUNG = "samsung";
    private static final String MANUFACTURER_LETV = "Letv";
    private static final String MANUFACTURER_ZTE = "ZTE";
    private static final String MANUFACTURER_YULONG = "YuLong";
    private static final String MANUFACTURER_LENOVO = "LENOVO";

    /**
     * This function can define your own
     *
     * @param activity
     */
    public static void GoToSetting(Activity activity) {
        switch (Build.MANUFACTURER) {
            case MANUFACTURER_HUAWEI:
                Huawei(activity);
                break;
            case MANUFACTURER_MEIZU:
                Meizu(activity);
                break;
            case MANUFACTURER_XIAOMI:
                Xiaomi(activity);
                break;
            case MANUFACTURER_SONY:
                Sony(activity);
                break;
            case MANUFACTURER_OPPO:
                OPPO(activity);
                break;
            case MANUFACTURER_LG:
                LG(activity);
                break;
            case MANUFACTURER_LETV:
                Letv(activity);
                break;
            case MANUFACTURER_VIVO:
                Vivo(activity);
                break;
            case MANUFACTURER_SAMSUNG:
                Samsung(activity);
                break;
            default:
                ApplicationInfo(activity);
                Log.e("goToSetting", "the current system does not support this");
                break;
        }
    }

    private static void Samsung(Activity activity) {
        String pkg = null;
        String classe = null;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
            pkg = "com.samsung.android.lool";
            // classe="com.samsung.android.sm.ui.battery.BatteryActivity";
        } else {
            pkg = "com.samsung.android.sm";
        }
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        try {
            ComponentName comp = new ComponentName(pkg, "com.samsung.android.sm.battery.ui.BatteryActivity");

            intent.setComponent(comp);
        } catch (Exception e) {

        }
        activity.startActivity(intent);

    }

    private static void Vivo(Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
            intent.setComponent(new ComponentName("com.iqoo.secure",
                    "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity"));
            activity.startActivity(intent);
        } catch (Exception e) {
            try {
                Intent intent = new Intent();
                intent.setComponent(new ComponentName("com.vivo.permissionmanager",
                        "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
                activity.startActivity(intent);
            } catch (Exception ex) {
                try {
                    Intent intent = new Intent();
                    intent.setClassName("com.iqoo.secure",
                            "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager");
                    activity.startActivity(intent);
                } catch (Exception exx) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private static void Huawei(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    private static void Meizu(Activity activity) {
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        activity.startActivity(intent);
    }

    private static void Xiaomi(Activity activity) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        ComponentName componentName = new ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.setComponent(componentName);
        intent.putExtra("extra_pkgname", BuildConfig.APPLICATION_ID);
        activity.startActivity(intent);
    }

    private static void Sony(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    private static void OPPO(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        // ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
        ComponentName comp = new ComponentName("com.coloros.safecenter", "com.coloros.privacypermissionsentry.PermissionTopActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    private static void LG(Activity activity) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    private static void Letv(Activity activity) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.PermissionAndApps");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    /**
     * Open only to bring their own security software
     *
     * @param activity
     */
    private static void _360(Activity activity) {
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("packageName", BuildConfig.APPLICATION_ID);
        ComponentName comp = new ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
        intent.setComponent(comp);
        activity.startActivity(intent);
    }

    /**
     * Application information interface
     *
     * @param activity
     */
    private static void ApplicationInfo(Activity activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
        }
        activity.startActivity(localIntent);
       /* if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // Permission denied
                if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE)){
                                         // If you do not check "Do not ask again", initiate a permission request to the user
                    ActivityCompat.requestPermissions((Activity) activity, new String[]{Manifest.permission.CALL_PHONE}, 0);
                }else{
                    //Before clicking "Do not ask again", the permission request box cannot be popped up again.
                                         // Can give Toast prompt, or Dialog feedback to the user, guide to open the corresponding permissions

                    // Go to the app information
                    Intent localIntent = new Intent();
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        localIntent.setAction(Intent.ACTION_VIEW);
                        localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
                        localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.getPackageName());
                    }
                }
            } else {
                // have permission, then you have to do the work
            }
        } else {
            // Before the system of 6.0, because the status of the permission could not be obtained, the operation requiring permission was directly executed.
        }*/
    }

    /**
     * System Settings interface
     *
     * @param activity
     */
    private static void SystemConfig(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
        activity.startActivity(intent);
    }
}
