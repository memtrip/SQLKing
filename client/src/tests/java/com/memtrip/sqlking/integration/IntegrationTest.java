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
import android.support.test.rule.ActivityTestRule;
import android.support.test.rule.UiThreadTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.memtrip.sqlking.database.SQLProvider;
import com.memtrip.sqlking.integration.utils.Setup;
import com.memtrip.sqlking.integration.utils.SetupData;
import com.memtrip.sqlking.integration.utils.SetupLog;
import com.memtrip.sqlking.integration.utils.SetupPost;
import com.memtrip.sqlking.integration.utils.SetupUser;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
@RunWith(AndroidJUnit4.class)
public abstract class IntegrationTest {

    @Rule
    public UiThreadTestRule startAppRule = new UiThreadTestRule();

    private Setup mSetup;
    private SetupUser mSetupUser;
    private SetupPost mSetupPost;
    private SetupLog mSetupLog;
    private SetupData mSetupData;

    protected SQLProvider getSQLProvider() {
        return mSetup.getSQLProvider();
    }

    SetupUser getSetupUser() {
        return mSetupUser;
    }

    SetupPost getSetupPost() {
        return mSetupPost;
    }

    SetupLog getSetupLog() {
        return mSetupLog;
    }

    SetupData getSetupData() {
        return mSetupData;
    }

    @Before
    public void setUp() {
        getInstrumentation().getTargetContext().deleteDatabase(Setup.DATABASE_NAME);

        mSetupUser = new SetupUser();
        mSetupPost = new SetupPost();
        mSetupLog = new SetupLog();
        mSetupData = new SetupData();

        mSetup = new Setup(getInstrumentation().getTargetContext());
        mSetup.setUp();
    }
}