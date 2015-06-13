package com.ataulm.tvlauncher;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;

public class App {

    public static final App NULL_SAFE = new App("", null, new Intent());

    private final String name;
    private final Drawable icon;
    private final Intent intent;

    App(String name, Drawable icon, Intent intent) {
        this.name = name;
        this.icon = icon;
        this.intent = intent;
    }

    public boolean isReal() {
        return !this.equals(NULL_SAFE);
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public Intent getIntent() {
        return intent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        App app = (App) o;
        return name.equals(app.name) && !(icon != null ? !icon.equals(app.icon) : app.icon != null) && intent.equals(app.intent);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + intent.hashCode();
        return result;
    }
    
}
