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

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import org.jetbrains.annotations.NotNull;

public class CodePagerAdapter extends PagerAdapter {

    @NotNull
    private final String[] mTitles;

    private CodeView mObjectPrinterCodeView;

    public CodePagerAdapter(@NotNull final Context context) {
        mTitles = context.getResources().getStringArray(R.array.titles);
    }

    public void setUseFailFast(final boolean useFailFast) {
        mObjectPrinterCodeView.setCode(useFailFast ? R.raw.objectprinter_notnull : R.raw.objectprinter);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(final View view, final Object o) {
        return view.equals(o);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        CodeView codeView = new CodeView(container.getContext());

        switch (position) {
            case 0:
                codeView.setCode(R.raw.mainactivity);
                break;
            case 1:
                codeView.setCode(R.raw.objectprinter);
                mObjectPrinterCodeView = codeView;
                break;
        }

        container.addView(codeView);
        return codeView;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return mTitles[position];
    }
}
