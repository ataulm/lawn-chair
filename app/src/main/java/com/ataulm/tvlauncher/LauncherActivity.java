package com.ataulm.tvlauncher;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class LauncherActivity extends Activity {

    private AppsRepository appsRepository;
    private AppsUsageDataRepository appsUsageDataRepository;
    private LauncherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        appsRepository = new AppsRepository(getPackageManager());
        appsUsageDataRepository = AppsUsageDataRepository.newInstance(this);

        onViewCreated();
    }

    private void onViewCreated() {
        Collection<App> apps = getAppsSortedByMostUsed();
        adapter = new LauncherAdapter(apps, createOnAppClickListener(), getLayoutInflater());

        RecyclerView appsRecyclerView = (RecyclerView) findViewById(R.id.launcher_recycler_apps);
        appsRecyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.launcher_columns)));
        appsRecyclerView.setAdapter(adapter);
    }

    private Collection<App> getAppsSortedByMostUsed() {
        List<App> apps = new ArrayList<>(appsRepository.fetchApps());
        Map<App, Integer> openCounts = appsUsageDataRepository.sortByMostUsed(apps);
        sortByOpenCounts(apps, openCounts);
        return apps;
    }

    private static void sortByOpenCounts(List<App> apps, final Map<App, Integer> openCounts) {
        Collections.sort(apps,
                new Comparator<App>() {

                    @Override
                    public int compare(App lhs, App rhs) {
                        Integer lhsCounts = openCounts.get(lhs);
                        Integer rhsCounts = openCounts.get(rhs);
                        return reverse(lhsCounts.compareTo(rhsCounts));
                    }

                    private int reverse(int compareResult) {
                        return -1 * compareResult;
                    }

                }
        );
    }

    private AppViewHolder.ClickListener createOnAppClickListener() {
        return new AppViewHolder.ClickListener() {
            @Override
            public void onClick(App app) {
                startActivity(app.getIntent());
                appsUsageDataRepository.onOpen(app);
                reorderAppsInLauncher();
            }
        };
    }

    private void reorderAppsInLauncher() {
        Collection<App> apps = getAppsSortedByMostUsed();
        adapter.update(apps);
    }

    @Override
    public void onBackPressed() {
    }

}
