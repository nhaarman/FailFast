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

import android.os.Bundle;

public class MainActivity extends BaseActivity {

    private static final String MESSAGE = "Hello, world!";

    private Object mObject;

    private ObjectPrinter mObjectPrinter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mObjectPrinter = new ObjectPrinter(this, MESSAGE);
    }

    @Override
    void print() {
        mObjectPrinter.print();
    }

    @Override
    void doStuff() {
        /* Bunch of other code */

        /* The next line introduces a bug,
           which can be hard to find: */
        mObjectPrinter.setObject(mObject);

        /* Bunch of other code */
    }

    @Override
    void setUseFailFast(final boolean useFailFast) {
        mObjectPrinter = useFailFast ? new FailFastObjectPrinter(this, MESSAGE) : new ObjectPrinter(this, MESSAGE);
    }
}
