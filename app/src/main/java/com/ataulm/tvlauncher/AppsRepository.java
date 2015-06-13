package com.ataulm.tvlauncher;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

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
        for (ApplicationInfo applicationInfo : getInstalledAppsFrom(packageManager)) {
            App app = obtainAppFrom(packageManager, applicationInfo);
            if (app.isReal()) {
                launchableInstalledApps.add(app);
            }
        }
        return launchableInstalledApps;
    }

    private static List<ApplicationInfo> getInstalledAppsFrom(PackageManager packageManager) {
        return packageManager.getInstalledApplications(PackageManager.PERMISSION_GRANTED);
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
