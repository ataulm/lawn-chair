package com.ataulm.tvlauncher;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class AppsRepository {

    private static final String CATEGORY_LEANBACK_SETTINGS = "android.intent.category.LEANBACK_SETTINGS";
    private final PackageManager packageManager;

    AppsRepository(PackageManager packageManager) {
        this.packageManager = packageManager;
    }

    public Collection<App> fetchApps() {
        return getAppsFrom(packageManager);
    }

    private static Collection<App> getAppsFrom(PackageManager packageManager) {
        Set<App> launchableInstalledApps = new HashSet<>();
        for (ResolveInfo resolveInfo : getInstalledAppsFrom(packageManager)) {
            App app = obtainAppFrom(packageManager, resolveInfo);
            if (app.getPackageName().toString().equals(BuildConfig.APPLICATION_ID)) {
                continue;
            }
            if (app.isReal()) {
                launchableInstalledApps.add(app);
            }
        }
        return launchableInstalledApps;
    }

    private static Set<ResolveInfo> getInstalledAppsFrom(PackageManager packageManager) {
        Set<ResolveInfo> activities = new HashSet<>();
        activities.addAll(launcherActivitiesIn(packageManager));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activities.addAll(leanbackActivitiesIn(packageManager));
            activities.addAll(leanbackSettingsActivitiesIn(packageManager));
        }
        return activities;
    }

    private static List<ResolveInfo> launcherActivitiesIn(PackageManager packageManager) {
        Intent intent = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LAUNCHER);
        return getResolveInfos(packageManager, intent);
    }

    private static List<ResolveInfo> getResolveInfos(PackageManager packageManager, Intent intent) {
        return packageManager.queryIntentActivities(intent, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static List<ResolveInfo> leanbackActivitiesIn(PackageManager packageManager) {
        Intent intent = new Intent(Intent.ACTION_MAIN)
                .addCategory(Intent.CATEGORY_LEANBACK_LAUNCHER);
        return getResolveInfos(packageManager, intent);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static List<ResolveInfo> leanbackSettingsActivitiesIn(PackageManager packageManager) {
        Intent intent = new Intent(Intent.ACTION_MAIN)
                .addCategory(CATEGORY_LEANBACK_SETTINGS);
        return getResolveInfos(packageManager, intent);
    }

    private static App obtainAppFrom(PackageManager packageManager, ResolveInfo resolveInfo) {
        ApplicationInfo applicationInfo = resolveInfo.activityInfo.applicationInfo;
        App.PackageName packageName = resolvePackageName(resolveInfo);

        String name = String.valueOf(packageManager.getApplicationLabel(applicationInfo));
        Drawable icon = packageManager.getApplicationIcon(applicationInfo);
        Intent launchIntent = launchIntent(packageManager, packageName.toString());

        return createApp(packageName, name, icon, launchIntent);
    }

    private static App.PackageName resolvePackageName(ResolveInfo resolveInfo) {
        String packageName = resolveInfo.activityInfo.packageName;
        if (packageName == null) {
            packageName = resolveInfo.resolvePackageName;
        }
        return new App.PackageName(packageName);
    }

    private static Intent launchIntent(PackageManager packageManager, String packageName) {
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            intent = leanbackLaunchIntent(packageManager, packageName);
        }
        return intent;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static Intent leanbackLaunchIntent(PackageManager packageManager, String packageName) {
        return packageManager.getLeanbackLaunchIntentForPackage(packageName);
    }

    private static App createApp(App.PackageName packageName, String appName, Drawable icon, Intent intent) {
        return App.from(packageName, appName, icon, intent);
    }

}
