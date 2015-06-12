package com.ataulm.tvlauncher;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

final class AppViewHolder extends RecyclerView.ViewHolder {

    private final ClickListener clickListener;

    static AppViewHolder inflate(LayoutInflater layoutInflater, ViewGroup parent, ClickListener clickListener) {
        View view = layoutInflater.inflate(R.layout.item_apps_app, parent, false);
        return new AppViewHolder(view, clickListener);
    }

    private AppViewHolder(View itemView, ClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
    }

    void bind(final App app) {
        ((TextView) itemView).setText(app.getName());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onClick(app);
            }
        });
    }

    interface ClickListener {

        void onClick(App app);

    }

}
