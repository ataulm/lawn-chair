package com.ataulm.tvlauncher;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        List<App> apps = getAppsFrom(getPackageManager());
        RecyclerView appsRecyclerView = (RecyclerView) findViewById(R.id.launcher_recycler_apps);
        appsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        AppsAdapter adapter = new AppsAdapter(getLayoutInflater(), new AppViewHolder.ClickListener() {
            @Override
            public void onClick(App app) {
                startActivity(app.getIntent());
            }
        });
        adapter.update(apps);
        appsRecyclerView.setAdapter(adapter);
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
        Intent launchIntent = packageManager.getLaunchIntentForPackage(applicationInfo.packageName);
        String name = String.valueOf(packageManager.getApplicationLabel(applicationInfo));
        return createApp(name, launchIntent);
    }

    private static App createApp(String appName, Intent intent) {
        if (intent == null) {
            return App.NULL_SAFE;
        }
        return new App(appName, intent);
    }

}
