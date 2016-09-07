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

import android.database.Cursor;

import com.memtrip.sqlking.integration.models.Data;
import com.memtrip.sqlking.integration.models.Log;
import com.memtrip.sqlking.operation.function.Raw;
import com.memtrip.sqlking.operation.function.Select;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Samuel Kirton [sam@memtrip.com]
 */
public class IndexTest extends IntegrationTest {

    @Before
    public void setUp() {
        super.setUp();

        getSetupData().tearDownTestData(getSQLProvider());
        getSetupData().setupTestData(getSQLProvider());

        getSetupLog().tearDownTestLogs(getSQLProvider());
        getSetupLog().setupTestLogs(getSQLProvider());
    }

    @Test
    public void testPostIndexesAreCreated() {
        Cursor cursor = Raw.getBuilder()
                .query("PRAGMA INDEX_LIST('Post');")
                .execute(getSQLProvider());

        List<String> indexes = getIndexes(cursor);

        assertEquals(1, indexes.size());
    }

    @Test
    public void testNoUserIndexesAreCreated() {
        Cursor cursor = Raw.getBuilder()
                .query("PRAGMA INDEX_LIST('User');")
                .execute(getSQLProvider());

        List<String> indexes = getIndexes(cursor);

        assertTrue(indexes.isEmpty());
    }

    @Test
    public void testAutoIncrementPrimaryKey() {
        Data[] data = Select.getBuilder().execute(Data.class, getSQLProvider());

        assertEquals(3, data.length);
        assertEquals(1, data[0].getId());
        assertEquals(2, data[1].getId());
        assertEquals(3, data[2].getId());
    }

    @Test
    public void testNoAutoIncrementPrimaryKey() {
        Log[] log = Select.getBuilder().execute(Log.class, getSQLProvider());
        assertEquals(3, log.length);
    }

    private List<String> getIndexes(Cursor cursor) {
        List<String> indexes = new ArrayList<>();

        try {
            while (cursor.moveToNext()) {
                int index = cursor.getColumnIndex("name");
                if (index != -1) {
                    String indexName = cursor.getString(index);
                    indexes.add(indexName);
                }
            }
        } finally {
            cursor.close();
        }

        return indexes;
    }
}
