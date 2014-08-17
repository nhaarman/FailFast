/*
 * Copyright 2014 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nhaarman.failfast.example;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

@SuppressWarnings("ProhibitedExceptionCaught")
public abstract class BaseActivity extends Activity {

    private static final String URL_GITHUB_IO = "http://nhaarman.github.io/FailFast?ref=app";

    private static final String PREFERENCE_FIRST_TIME = "first_time";

    private StackTraceView mStackTraceView;

    private CodePagerAdapter mCodePagerAdapter;

    private boolean mFailFastEnabled;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mCodePagerAdapter = new CodePagerAdapter(this);
        ((ViewPager) findViewById(R.id.activity_main_viewpager)).setAdapter(mCodePagerAdapter);

        mStackTraceView = (StackTraceView) findViewById(R.id.activity_main_stacktraceview);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean(PREFERENCE_FIRST_TIME, true)) {
            startActivity(new Intent(this, HelpActivity.class));
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean(PREFERENCE_FIRST_TIME, false).apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.menu_main_toggle);
        item.setIcon(mFailFastEnabled ? R.drawable.ic_menu_toggle_enabled : R.drawable.ic_menu_toggle_disabled);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_main_toggle:
                mFailFastEnabled = !mFailFastEnabled;
                mCodePagerAdapter.setUseFailFast(mFailFastEnabled);
                setUseFailFast(mFailFastEnabled);
                invalidateOptionsMenu();

                Toast.makeText(this, mFailFastEnabled ? getString(R.string.enabled_failfast) : getString(R.string.disabled_failfast), Toast.LENGTH_SHORT).show();

                return true;
            case R.id.menu_main_help:
                startActivity(new Intent(this, HelpActivity.class));
                return true;
            case R.id.menu_main_github:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URL_GITHUB_IO));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    abstract void print();

    abstract void doStuff();

    abstract void setUseFailFast(boolean useFailFast);

    public void print(final View view) {
        try {
            print();
        } catch (NullPointerException ignored) {
            mStackTraceView.handleNormalNullPointerException();
        }
    }

    public void doStuff(final View view) {
        try {
            doStuff();
            Toast.makeText(this, getString(R.string.did_stuff), Toast.LENGTH_SHORT).show();
        } catch (NullPointerException ignored) {
            mStackTraceView.handleFailFastNullPointerException();
        }
    }
}
