package com.example.jawwy.model.db

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import androidx.test.filters.SmallTest
import com.example.jawwy.model.data.JsonPojo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class WeatherLocalDataSourceTest {
//    private lateinit var dataBase:AppDataBase
//    private lateinit var dao:WeatherDao
    private lateinit var localDataSource: IWeatherLocalDataSource

    @get:Rule
    val rule = InstantTaskExecutorRule()
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp(){
//        dataBase = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(),AppDataBase::class.java).allowMainThreadQueries().build()
//        dao = dataBase.weatherDao()
        localDataSource=WeatherLocalDataSource.getInstance(ApplicationProvider.getApplicationContext())
    }
    @After
    fun tearDown(){
        //dataBase.close()
    }

    @Test
    fun insertWeatherandGetById() = mainCoroutineRule.runBlockingTest {
        val w1 = JsonPojo(id = "101", gps = "no", lat = 10.123)
        localDataSource.insert(w1)

        val loaded = localDataSource.getWeatherById("101").first()

        MatcherAssert.assertThat(loaded.lat, CoreMatchers.`is`(10.123))

    }
    @Test
    fun insertmultipleWeatherandGetAll() = mainCoroutineRule.runBlockingTest {
        val w1 = JsonPojo(id = "101", gps = "no", lat = 10.123)
        val w2 = JsonPojo(id = "102", gps = "yes", lat = 17.123)
        val w3 = JsonPojo(id = "103", gps = "yes", lat = 12.123)
        val w4 = JsonPojo(id = "104", gps = "no", lat = 11.879)
        localDataSource.insert(w1)
        localDataSource.insert(w2)
        localDataSource.insert(w3)
        localDataSource.insert(w4)

        val loaded = localDataSource.getAllWeather().first()

        MatcherAssert.assertThat(loaded.size, CoreMatchers.`is`(4))
        MatcherAssert.assertThat(loaded[1].gps, CoreMatchers.`is`("yes"))

    }

    @Test
    fun insertmultipleWeatherandDeleteOne() = mainCoroutineRule.runBlockingTest {
        val w1 = JsonPojo(id = "101", gps = "no", lat = 10.123)
        val w2 = JsonPojo(id = "102", gps = "yes", lat = 17.123)
        val w3 = JsonPojo(id = "103", gps = "yes", lat = 12.123)
        val w4 = JsonPojo(id = "104", gps = "no", lat = 11.879)
        localDataSource.insert(w1)
        localDataSource.insert(w2)
        localDataSource.insert(w3)
        localDataSource.insert(w4)

        val result = localDataSource.delete(w3)

        val loaded = localDataSource.getAllWeather().first()

        MatcherAssert.assertThat(result, CoreMatchers.`is`(1))
        MatcherAssert.assertThat(loaded.size, CoreMatchers.`is`(3))

    }

    @Test
    fun insertmultipleWeatherandDeleteByGps() = mainCoroutineRule.runBlockingTest {
        val w1 = JsonPojo(id = "101", gps = "no", lat = 10.123)
        val w2 = JsonPojo(id = "102", gps = "yes", lat = 17.123)
        val w3 = JsonPojo(id = "103", gps = "yes", lat = 12.123)
        val w4 = JsonPojo(id = "104", gps = "no", lat = 11.879)
        localDataSource.insert(w1)
        localDataSource.insert(w2)
        localDataSource.insert(w3)
        localDataSource.insert(w4)

        val result = localDataSource.deleteFromGps()

        val loaded = localDataSource.getAllWeather().first()

        MatcherAssert.assertThat(result, CoreMatchers.`is`(2))
        MatcherAssert.assertThat(loaded.size, CoreMatchers.`is`(2))

    }

}