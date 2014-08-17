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

/* http://stackoverflow.com/a/19787125/675383 */

package com.nhaarman.failfast.example;

import android.content.Context;
import android.content.res.Resources;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import prettify.PrettifyParser;
import syntaxhighlight.ParseResult;
import syntaxhighlight.Parser;

public class PrettifyHighlighter {

    @NotNull
    private static final String FONT_PATTERN = "<font color=\"#%s\">%s</font>";

    @NotNull
    private final Map<String, String> mColorsMap = new HashMap<>();

    @Nullable
    private Parser mParser;

    @NotNull
    private final Context mContext;

    public PrettifyHighlighter(@NotNull final Context context) {
        mContext = context;
    }

    public String highlight(String fileExtension, String sourceCode) {
        if (mParser == null) {
            mParser = new PrettifyParser();
        }

        StringBuilder highlighted = new StringBuilder();
        List<ParseResult> results = mParser.parse(fileExtension, sourceCode);

        for (ParseResult result : results) {
            String content = sourceCode.substring(result.getOffset(), result.getOffset() + result.getLength()).replaceAll("\\n", "<br/>").replaceAll(" ", "&nbsp;");
            String type = content.equals(";") || content.equals(",") ? "semicolon" : result.getStyleKeys().get(0);
            highlighted.append(String.format(FONT_PATTERN, getColor(type), content));
        }

        return highlighted.toString();
    }

    private String getColor(String type) {
        if (mColorsMap.containsKey(type)) {
            return mColorsMap.get(type);
        } else {
            Resources resources = mContext.getResources();
            String color = resources.getString(resources.getIdentifier("code_" + type, "color", mContext.getPackageName())).substring(3);
            mColorsMap.put(type, color);
            return color;
        }
    }
}