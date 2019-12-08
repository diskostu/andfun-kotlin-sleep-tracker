/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.trackmysleepquality

// TODO (07) Uncomment the code in this file, then run the tests.

// TODO (08) Optional: Add tests to exercise the other DAO methods.

import android.util.Log
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.trackmysleepquality.database.SleepDatabase
import com.example.android.trackmysleepquality.database.SleepDatabaseDao
import com.example.android.trackmysleepquality.database.SleepNight
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

/**
 * This is not meant to be a full set of tests. For simplicity, most of your samples do not
 * include tests. However, when building the Room, it is helpful to make sure it works before
 * adding the UI.
 */

@RunWith(AndroidJUnit4::class)
class SleepDatabaseTest {

    private lateinit var sleepDao: SleepDatabaseDao
    private lateinit var db: SleepDatabase

    private val TAG = "TEST1"

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, SleepDatabase::class.java)
                // Allowing main thread queries, just for testing.
                .allowMainThreadQueries()
                .build()
        sleepDao = db.sleepDatabaseDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNight() {
        val night = SleepNight()
        sleepDao.insert(night)
        val tonight = sleepDao.getTonight()
        assertEquals(tonight?.sleepQuality, -1)
    }


    /**
     * We have no nights, but try to get the latest night. The result must be null.
     */
    @Test
    fun getNightError() {
        val tonight = sleepDao.getTonight()
        assertNull(tonight)
    }


    @Test
    fun updateAndGetNight() {
        sleepDao.insert(SleepNight(sleepQuality = 3))
        val tonight = sleepDao.getTonight()

        assertNotNull(tonight)
        if (tonight != null) {
            assertEquals(3, tonight.sleepQuality)

            tonight.sleepQuality = 0
            assertEquals(0, tonight.sleepQuality)
        }
    }


    /**
     * We insert 1 item and assure the tonight is not null. Then we clear the database and assure
     * afterwards that tonight is null.
     */
    @Test
    fun clearAndGet() {
        sleepDao.insert(SleepNight())
        assertNotNull(sleepDao.getTonight())

        sleepDao.clear()
        assertNull(sleepDao.getTonight())
    }
}