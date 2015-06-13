package com.ataulm.tvlauncher;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class App {

    public static final App NULL_SAFE = new App(new PackageName(""), "", null, new Intent());

    private final PackageName packageName;
    private final String name;
    private final Drawable icon;
    private final Intent intent;

    App(PackageName packageName, String name, Drawable icon, Intent intent) {
        this.packageName = packageName;
        this.name = name;
        this.icon = icon;
        this.intent = intent;
    }

    public boolean isReal() {
        return !this.equals(NULL_SAFE);
    }

    public PackageName getPackageName() {
        return packageName;
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

        if (!packageName.equals(app.packageName)) {
            return false;
        }
        if (!name.equals(app.name)) {
            return false;
        }
        if (icon != null ? !icon.equals(app.icon) : app.icon != null) {
            return false;
        }
        return intent.equals(app.intent);
    }

    @Override
    public int hashCode() {
        int result = packageName.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + (icon != null ? icon.hashCode() : 0);
        result = 31 * result + intent.hashCode();
        return result;
    }

    static class PackageName {

        private final String packageName;

        PackageName(String packageName) {
            this.packageName = packageName;
        }

        @Override
        public String toString() {
            return packageName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            PackageName that = (PackageName) o;

            return packageName.equals(that.packageName);

        }

        @Override
        public int hashCode() {
            return packageName.hashCode();
        }

    }

}
