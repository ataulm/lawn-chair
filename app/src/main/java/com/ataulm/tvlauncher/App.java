package com.ataulm.tvlauncher;

import android.content.Intent;

public class App {

    public static final App NULL_SAFE = new App("", new Intent());

    private final String name;
    private final Intent intent;

    App(String name, Intent intent) {
        this.name = name;
        this.intent = intent;
    }

    public boolean isReal() {
        return !this.equals(NULL_SAFE);
    }

    public String getName() {
        return name;
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
        return name.equals(app.name) && intent.equals(app.intent);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + intent.hashCode();
        return result;
    }

}
