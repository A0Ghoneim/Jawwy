package com.example.jawwy.model.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.jawwy.Favourites.MainCoroutineRule
import com.example.jawwy.model.data.JsonPojo
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.hamcrest.core.IsEqual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeatherRepositoryTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    var mainCoroutineRule = MainCoroutineRule()
    private val w1 = JsonPojo(id = "101", gps = "no" )
    private val w2 = JsonPojo(id = "102", gps = "no")
    private val w3 = JsonPojo(id = "103", gps = "no")
    private val w4 = JsonPojo(id = "104", gps = "no", lat = 10.123, lon =15.456)
    private val localWeather = listOf<JsonPojo>(w1,w2,w3)
    lateinit var weatherRemoteDataSource: FakeRemoteDataSource
    lateinit var weatherLocalDataSource: FakeLocalDataSource
    lateinit var sharedPreferenceDatasource: FakeSharedPreferencesDataSource

    lateinit var weatherRepository: IWeatherRepository

    @Before
    fun createRepository(){
        weatherRemoteDataSource= FakeRemoteDataSource(w4)
        weatherLocalDataSource= FakeLocalDataSource(localWeather.toMutableList())
        sharedPreferenceDatasource = FakeSharedPreferencesDataSource()
        sharedPreferenceDatasource.mylanguage="ar"

        weatherRepository= WeatherRepository(weatherRemoteDataSource,weatherLocalDataSource,sharedPreferenceDatasource)
    }
    @Test
    fun getWeather_latLong_jsonPojo()=mainCoroutineRule.runBlockingTest {
       val result =  weatherRepository.getWeather(10.123,15.456).first()
            MatcherAssert.assertThat(result.body()?.id, IsEqual(w4.id))
    }
    @Test
    fun getWeather_latLong_null() =mainCoroutineRule.runBlockingTest{
        val result = weatherRepository.getWeather(1.0,7.0).first()
        assertThat(result.code(), `is`(400))
    }

    @Test
    fun delete_jsonPojo_one() =mainCoroutineRule.runBlockingTest{
        val result = weatherRepository.delete(w2)
        assertThat(result, `is`(1))
    }
    @Test
    fun delete_jsonPojo_zero() =mainCoroutineRule.runBlockingTest{
        val result = weatherRepository.delete(w4)
        assertThat(result, `is`(0))
    }

    @Test
    fun insert_jsonPojo_one() =mainCoroutineRule.runBlockingTest{
        val result = weatherRepository.insert(w2)
        assertThat(result, `is`(1L))
    }

    @Test
    fun getLanguage() {
        val result = weatherRepository.getLanguage()
        assertThat(result, `is`("ar"))
    }

    @Test
    fun putLanguage() {
        weatherRepository.putLanguage("fr")
        val result = weatherRepository.getLanguage()
        assertThat(result, `is`("fr"))
    }
}