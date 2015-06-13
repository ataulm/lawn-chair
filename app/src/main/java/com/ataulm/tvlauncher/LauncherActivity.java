package com.ataulm.tvlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        List<App> apps = new AppsRepository(getPackageManager()).fetchApps();
        AppViewHolder.ClickListener clickListener = new AppViewHolder.ClickListener() {
            @Override
            public void onClick(App app) {
                startActivity(app.getIntent());
            }
        };
        RecyclerView appsRecyclerView = (RecyclerView) findViewById(R.id.launcher_recycler_apps);
        RecyclerView.Adapter adapter = new AppsAdapter(apps, clickListener, getLayoutInflater());
        appsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        appsRecyclerView.setAdapter(adapter);
    }

}
