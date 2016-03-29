/**
 * Copyright 2013-present memtrip LTD.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.memtrip.sqlking.integration;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.memtrip.sqlking.database.SQLProvider;
import com.memtrip.sqlking.integration.utils.Setup;
import com.memtrip.sqlking.integration.utils.SetupUser;

import org.junit.Before;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
public abstract class IntegrationTest extends ActivityInstrumentationTestCase2<Activity> {
    private Setup mSetup;
    private SetupUser mSetupUser;

    protected SQLProvider getSQLDatabase() {
        return mSetup.getSQLDatabase();
    }

    protected SetupUser getSetupUser() {
        return mSetupUser;
    }

    @Before
    public void setUp() {
        getInstrumentation().getTargetContext().deleteDatabase(Setup.DATABASE_NAME);

        mSetup = new Setup(getInstrumentation().getTargetContext());
        mSetupUser = new SetupUser();
        mSetup.setUp();
    }

    public IntegrationTest() {
        super(Activity.class);
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
    }
}