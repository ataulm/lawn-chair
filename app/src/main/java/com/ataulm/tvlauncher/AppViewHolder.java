package com.ataulm.tvlauncher;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

final class AppViewHolder extends RecyclerView.ViewHolder {

    private final ImageView iconView;
    private final TextView nameView;
    private final ClickListener clickListener;

    static AppViewHolder inflate(LayoutInflater layoutInflater, ViewGroup parent, ClickListener clickListener) {
        View view = layoutInflater.inflate(R.layout.item_launcher_app, parent, false);
        ImageView iconView = (ImageView) view.findViewById(R.id.app_image_icon);
        TextView nameView = (TextView) view.findViewById(R.id.app_text_name);
        return new AppViewHolder(view, iconView, nameView, clickListener);
    }

    private AppViewHolder(View itemView, ImageView iconView, TextView nameView, ClickListener clickListener) {
        super(itemView);
        this.iconView = iconView;
        this.nameView = nameView;
        this.clickListener = clickListener;
    }

    void bind(final App app) {
        nameView.setText(app.getName());
        iconView.setImageDrawable(app.getIcon());
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
