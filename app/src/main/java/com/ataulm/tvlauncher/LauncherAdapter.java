package com.ataulm.tvlauncher;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

final class LauncherAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private final List<App> apps;
    private final AppViewHolder.ClickListener clickListener;
    private final LayoutInflater layoutInflater;

    LauncherAdapter(Collection<App> apps, AppViewHolder.ClickListener clickListener, LayoutInflater layoutInflater) {
        this.apps = new ArrayList<>(apps);
        this.clickListener = clickListener;
        this.layoutInflater = layoutInflater;

        setHasStableIds(true);
    }

    @Override
    public AppViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return AppViewHolder.inflate(layoutInflater, parent, clickListener);
    }

    @Override
    public void onBindViewHolder(AppViewHolder holder, int position) {
        App app = apps.get(position);
        holder.bind(app);
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    @Override
    public long getItemId(int position) {
        return apps.get(position).hashCode();
    }

    void update(Collection<App> apps) {
        this.apps.clear();
        this.apps.addAll(apps);
        notifyDataSetChanged();
    }

}
