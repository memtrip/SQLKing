package com.beyondconstraint.sqlking.integration;

import android.app.Activity;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import com.beyondconstraint.sqlking.database.SQLProvider;
import com.beyondconstraint.sqlking.integration.utils.Setup;
import com.beyondconstraint.sqlking.integration.utils.SetupUser;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class IntegrationTest extends ActivityInstrumentationTestCase2<Activity> {
    private Setup mSetup;
    private SetupUser mSetupUser;

    protected SQLProvider getSQLProvider() {
        return mSetup.getSQLProvider();
    }

    protected SetupUser getSetupUser() {
        return mSetupUser;
    }

    public IntegrationTest() {
        super(Activity.class);
        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
        mSetup = new Setup(getInstrumentation().getTargetContext());
        mSetupUser = new SetupUser();

        mSetup.setUp();
    }
}