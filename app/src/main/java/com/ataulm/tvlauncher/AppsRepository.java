package com.ataulm.tvlauncher;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class AppsRepository {

    private final PackageManager packageManager;

    AppsRepository(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public List<App> fetchApps() {
        return getAppsFrom(packageManager);
    }

    private static List<App> getAppsFrom(PackageManager packageManager) {
        List<App> launchableInstalledApps = new ArrayList<>();
        for (ResolveInfo resolveInfo : getInstalledAppsFrom(packageManager)) {
            App app = obtainAppFrom(packageManager, resolveInfo.activityInfo.applicationInfo);
            if (app.isReal()) {
                launchableInstalledApps.add(app);
            }
        }
        return launchableInstalledApps;
    }

    private static Set<ResolveInfo> getInstalledAppsFrom(PackageManager packageManager) {
        Set<ResolveInfo> activities = new HashSet<>();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            activities.addAll(launcherActivitiesIn(packageManager));
        } else {
            activities.addAll(leanbackActivitiesIn(packageManager));
        }
        return activities;
    }

    private static List<ResolveInfo> launcherActivitiesIn(PackageManager packageManager) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return packageManager.queryIntentActivities(intent, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static List<ResolveInfo> leanbackActivitiesIn(PackageManager packageManager) {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        return packageManager.queryIntentActivities(intent, 0);
    }

    private static App obtainAppFrom(PackageManager packageManager, ApplicationInfo applicationInfo) {
        App.PackageName packageName = new App.PackageName(applicationInfo.packageName);
        String name = String.valueOf(packageManager.getApplicationLabel(applicationInfo));
        Drawable icon = packageManager.getApplicationIcon(applicationInfo);
        Intent launchIntent = packageManager.getLaunchIntentForPackage(applicationInfo.packageName);

        return createApp(packageName, name, icon, launchIntent);
    }

    private static App createApp(App.PackageName packageName, String appName, Drawable icon, Intent intent) {
        if (intent == null) {
            return App.NULL_SAFE;
        }
        return new App(packageName, appName, icon, intent);
    }

}
