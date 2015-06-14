package com.ataulm.tvlauncher;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class AppsUsageDataRepository {

    private static final String PREFERENCE_NAME = BuildConfig.APPLICATION_ID + ".MostUsedApps";
    private static final String KEY_OPEN_COUNT_PREFIX = BuildConfig.APPLICATION_ID + ".OpenCount.";

    private final SharedPreferences preferences;

    static AppsUsageDataRepository newInstance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return new AppsUsageDataRepository(sharedPreferences);
    }

    private AppsUsageDataRepository(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    void onOpen(App app) {
        int openCount = preferences.getInt(keyOpenCountFor(app), 0);
        preferences.edit().putInt(keyOpenCountFor(app), openCount + 1).apply();
    }

    Map<App, Integer> sortByMostUsed(List<App> apps) {
        return getOpenCountsFor(apps, preferences);
    }

    private static Map<App, Integer> getOpenCountsFor(Collection<App> apps, SharedPreferences preferences) {
        Map<App, Integer> openCounts = new HashMap<>(apps.size());
        for (App app : apps) {
            int numberOfTimesOpened = preferences.getInt(keyOpenCountFor(app), 0);
            openCounts.put(app, numberOfTimesOpened);
        }
        return openCounts;
    }

    private static String keyOpenCountFor(App app) {
        return KEY_OPEN_COUNT_PREFIX + app.getPackageName().toString();
    }

}
