package com.ataulm.tvlauncher;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

final class AppUsageDataRepository {

    private static final String PREFERENCE_NAME = BuildConfig.APPLICATION_ID + ".MostUsedApps";
    private static final String KEY_PACKAGE_NAMES = BuildConfig.APPLICATION_ID + "PACKAGE_NAMES";
    private static final String KEY_OPEN_COUNT_PREFIX = BuildConfig.APPLICATION_ID + ".OpenCount.";

    private final SharedPreferences preferences;

    static AppUsageDataRepository newInstance(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        return new AppUsageDataRepository(sharedPreferences);
    }

    private AppUsageDataRepository(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    void onOpen(App app) {
        String packageName = app.getPackageName().toString();
        int openCount = preferences.getInt(keyOpenCountFor(packageName), 0);
        preferences.edit().putInt(keyOpenCountFor(packageName), openCount + 1).apply();
    }

    List<App.PackageName> getMostUsedApps(int sizeCap) {
        List<String> apps = getListOfAppsFrom(preferences);
        Map<String, Integer> openCounts = getOpenCountsFor(apps, preferences);
        sortByOpenCounts(apps, openCounts);
        return asCappedListOfMostUsedApps(apps, sizeCap);
    }

    private static List<String> getListOfAppsFrom(SharedPreferences preferences) {
        Set<String> apps = preferences.getStringSet(KEY_PACKAGE_NAMES, Collections.<String>emptySet());
        List<String> appsList = new ArrayList<>(apps.size());
        appsList.addAll(apps);
        return appsList;
    }

    private static Map<String, Integer> getOpenCountsFor(Collection<String> apps, SharedPreferences preferences) {
        Map<String, Integer> openCounts = new HashMap<>(apps.size());
        for (String app : apps) {
            openCounts.put(app, preferences.getInt(keyOpenCountFor(app), 0));
        }
        return openCounts;
    }

    private static void sortByOpenCounts(List<String> apps, final Map<String, Integer> openCounts) {
        Collections.sort(apps, new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                Integer lhsCounts = openCounts.get(lhs);
                Integer rhsCounts = openCounts.get(rhs);
                return lhsCounts.compareTo(rhsCounts);
            }
        });
    }

    private static List<App.PackageName> asCappedListOfMostUsedApps(List<String> apps, int size) {
        List<App.PackageName> mostUsedApps = new ArrayList<>(size);
        for (int i = 0; i < size && i < apps.size(); i++) {
            mostUsedApps.add(new App.PackageName(apps.get(i)));
        }
        return mostUsedApps;
    }

    private static String keyOpenCountFor(String packageName) {
        return KEY_OPEN_COUNT_PREFIX + packageName;
    }

}
