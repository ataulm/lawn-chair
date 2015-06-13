package com.ataulm.tvlauncher;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

final class AppsAdapter extends RecyclerView.Adapter<AppViewHolder> {

    private final List<App> apps;
    private final AppViewHolder.ClickListener clickListener;
    private final LayoutInflater layoutInflater;

    AppsAdapter(List<App> apps, AppViewHolder.ClickListener clickListener, LayoutInflater layoutInflater) {
        this.apps = apps;
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
        holder.bind(apps.get(position));
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    @Override
    public long getItemId(int position) {
        return apps.get(position).hashCode();
    }

}
