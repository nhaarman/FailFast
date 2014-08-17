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
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StackTraceView extends LinearLayout {

    private final TextView mTitleTV;

    private final TextView mStackTraceTV;

    private float mDownY;

    public StackTraceView(@NotNull final Context context) {
        this(context, null);
    }

    public StackTraceView(@NotNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StackTraceView(@NotNull final Context context, @Nullable final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.view_stacktrace, this, true);
        mTitleTV = (TextView) findViewById(R.id.view_stacktrace_titletv);
        mStackTraceTV = (TextView) findViewById(R.id.view_stacktrace_stacktracetv);
    }

    public void handleNormalNullPointerException() {
        mStackTraceTV.setText(R.string.stacktrace_normal);
        showHint();
    }

    public void handleFailFastNullPointerException() {
        mStackTraceTV.setText(R.string.stacktrace_failfast);
        showHint();
    }

    private void showHint() {
        setVisibility(VISIBLE);
        getViewTreeObserver().addOnPreDrawListener(new HintAnimator());
    }


    @Override
    public boolean onTouchEvent(@NotNull final MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float newY = getY() + (event.getY() - mDownY);
                if (newY > 0) {
                    newY = 0;
                }
                setY(newY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (getY() > -getHeight() / 2) {
                    animate().y(0).setInterpolator(new AccelerateDecelerateInterpolator());
                } else {
                    animate().y(-getHeight()).setInterpolator(new AccelerateDecelerateInterpolator());
                }
        }

        return true;
    }

    private class HintAnimator implements ViewTreeObserver.OnPreDrawListener {

        @Override
        public boolean onPreDraw() {
            int animateHeight = (int) (getHeight() - mTitleTV.getY());

            getViewTreeObserver().removeOnPreDrawListener(this);
            setY(-getHeight());
            animate().y(-getHeight() + animateHeight).setInterpolator(new OvershootInterpolator());
            return true;
        }
    }
}
