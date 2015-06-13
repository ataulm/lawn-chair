package com.ataulm.tvlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class LauncherActivity extends Activity {

    private AppUsageDataRepository appUsageDataRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        appUsageDataRepository = AppUsageDataRepository.newInstance(this);

        List<App> apps = new AppsRepository(getPackageManager()).fetchApps();
        RecyclerView appsRecyclerView = (RecyclerView) findViewById(R.id.launcher_recycler_apps);
        RecyclerView.Adapter adapter = new LauncherAdapter(apps, createOnAppClickListener(), getLayoutInflater());
        appsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        appsRecyclerView.setAdapter(adapter);
    }

    private AppViewHolder.ClickListener createOnAppClickListener() {
        return new AppViewHolder.ClickListener() {
            @Override
            public void onClick(App app) {
                appUsageDataRepository.onOpen(app);
                startActivity(app.getIntent());
            }
        };
    }

}
