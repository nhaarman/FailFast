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
import android.widget.Toast;

/**
 * A class for printing objects to a
 * Toast message.
 */
public class ObjectPrinter {

    private final Context mContext;

    private Object mObject;

    public ObjectPrinter(Context context, Object object) {
        mContext = context;
        mObject = object;
    }

    public void setObject(Object object) {
        mObject = object;
    }

    public void print() {
        Toast.makeText(mContext, mObject.toString(), Toast.LENGTH_LONG).show();
    }
}
