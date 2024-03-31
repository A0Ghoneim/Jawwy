package com.example.jawwy.model.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.jawwy.model.data.JsonPojo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest

class WeatherDaoTest {
    private lateinit var dataBase:AppDataBase
    private lateinit var dao:WeatherDao

    @get:Rule
    val rule = InstantTaskExecutorRule()
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp(){
        dataBase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),AppDataBase::class.java).build()
        dao = dataBase.weatherDao()
    }
    @After
    fun tearDown(){
        dataBase.close()
    }
    @Test
    fun insertWeatherandGetById() = mainCoroutineRule.runBlockingTest {
        val w1 = JsonPojo(id = "101", gps = "no", lat = 10.123)
        dao.insertWeather(w1)

        val loaded = dao.getWeatherById("101").first()

        assertThat(loaded.lat,`is`(10.123))

    }
    @Test
    fun insertmultipleWeatherandGetAll() = mainCoroutineRule.runBlockingTest {
        val w1 = JsonPojo(id = "101", gps = "no", lat = 10.123)
        val w2 = JsonPojo(id = "102", gps = "yes", lat = 17.123)
        val w3 = JsonPojo(id = "103", gps = "yes", lat = 12.123)
        val w4 = JsonPojo(id = "104", gps = "no", lat = 11.879)
        dao.insertWeather(w1)
        dao.insertWeather(w2)
        dao.insertWeather(w3)
        dao.insertWeather(w4)

        val loaded = dao.getAllWeather().first()

        assertThat(loaded.size,`is`(4))
        assertThat(loaded[1].gps,`is`("yes"))

    }

    @Test
    fun insertmultipleWeatherandDeleteOne() = mainCoroutineRule.runBlockingTest {
        val w1 = JsonPojo(id = "101", gps = "no", lat = 10.123)
        val w2 = JsonPojo(id = "102", gps = "yes", lat = 17.123)
        val w3 = JsonPojo(id = "103", gps = "yes", lat = 12.123)
        val w4 = JsonPojo(id = "104", gps = "no", lat = 11.879)
        dao.insertWeather(w1)
        dao.insertWeather(w2)
        dao.insertWeather(w3)
        dao.insertWeather(w4)

        val result = dao.deleteWeather(w3)

        val loaded = dao.getAllWeather().first()

        assertThat(result,`is`(1))
        assertThat(loaded.size,`is`(3))

    }

    @Test
    fun insertmultipleWeatherandDeleteByGps() = mainCoroutineRule.runBlockingTest {
        val w1 = JsonPojo(id = "101", gps = "no", lat = 10.123)
        val w2 = JsonPojo(id = "102", gps = "yes", lat = 17.123)
        val w3 = JsonPojo(id = "103", gps = "yes", lat = 12.123)
        val w4 = JsonPojo(id = "104", gps = "no", lat = 11.879)
        dao.insertWeather(w1)
        dao.insertWeather(w2)
        dao.insertWeather(w3)
        dao.insertWeather(w4)

        val result = dao.deleteFromGps()

        val loaded = dao.getAllWeather().first()

        assertThat(result,`is`(2))
        assertThat(loaded.size,`is`(2))

    }

}