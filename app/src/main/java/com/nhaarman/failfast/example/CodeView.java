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
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.text.Html;
import android.text.Spanned;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;

public class CodeView extends FrameLayout {

    private static final String JAVA = "java";

    @NotNull
    private final Handler mHandler;

    @NotNull
    private final PrettifyHighlighter mPrettifyHighlighter;

    public CodeView(@NotNull final Context context) {
        this(context, null);
    }

    public CodeView(@NotNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CodeView(@NotNull final Context context, @Nullable final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);

        LayoutInflater.from(context).inflate(R.layout.view_code, this, true);
        TextView textView = (TextView) findViewById(R.id.view_code_textview);
        mPrettifyHighlighter = new PrettifyHighlighter(context);

        mHandler = new MyHandler(textView);
    }

    public void setCode(final String code) {
        new Thread(new JavaPrettifierRunnable(code)).start();
    }

    public void setCode(@IdRes final int codeResId) {
        try {
            Resources res = getResources();
            InputStream inputStream = res.openRawResource(codeResId);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            setCode(new String(bytes, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class MyHandler extends Handler {

        @NotNull
        private final TextView mTextView;

        private MyHandler(@NotNull final TextView textView) {
            mTextView = textView;
        }

        @Override
        public void handleMessage(final Message msg) {
            setText((CharSequence) msg.obj);
        }

        public void setText(@NotNull final CharSequence text) {
            if (mTextView.getText().toString().isEmpty()) {
                mTextView.setAlpha(0);
                new SetTextRunnable(text, mTextView).run();
            } else {
                mTextView.animate()
                         .alpha(0)
                         .withEndAction(new SetTextRunnable(text, mTextView));
            }
        }
    }

    private static class SetTextRunnable implements Runnable {

        @NotNull
        private final CharSequence mText;

        @NotNull
        private final TextView mTextView;

        SetTextRunnable(@NotNull final CharSequence text, @NotNull final TextView textView) {
            mText = text;
            mTextView = textView;
        }

        @Override
        public void run() {
            mTextView.setText(mText);
            mTextView.animate().alpha(1);
        }
    }

    private class JavaPrettifierRunnable implements Runnable {

        @NotNull
        private final String mCode;

        private JavaPrettifierRunnable(@NotNull final String code) {
            mCode = code;
        }

        @Override
        public void run() {
            String htmlJava = mPrettifyHighlighter.highlight(JAVA, mCode);

            Spanned spannedHtml = Html.fromHtml(htmlJava);
            Message message = mHandler.obtainMessage(0, spannedHtml);
            message.sendToTarget();
        }
    }
}
