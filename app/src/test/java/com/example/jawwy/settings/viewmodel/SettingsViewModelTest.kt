package com.example.jawwy.settings.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jawwy.Favourites.FakeRepository
import com.example.jawwy.model.repo.IWeatherRepository
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config

@Config(manifest= Config.NONE)
@RunWith(AndroidJUnit4::class)
class SettingsViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    lateinit var repo: IWeatherRepository
    lateinit var viewModel: SettingsViewModel


        @Before
        fun setup(){
            repo= FakeRepository()
            viewModel=SettingsViewModel(repo)
        }



    @Test
    fun getDefaultUnit() {
        val result = viewModel.getUnit()
        MatcherAssert.assertThat(result, `is`("standard"))
    }
    @Test
    fun putUnitAndRetrieve() {
        viewModel.putUnit("imparial")
        val result = viewModel.getUnit()
        MatcherAssert.assertThat(result, `is`("imparial"))
    }

    @Test
    fun getDefaultLanguage() {
        val result = viewModel.getLanguage()
        MatcherAssert.assertThat(result, `is`("en"))
    }

    @Test
    fun putLanguageAndRetrieve() {
        viewModel.putLanguage("fr")
        val result = viewModel.getLanguage()
        MatcherAssert.assertThat(result, `is`("fr"))
    }

    @Test
    fun getDefaultLocationSettings() {
        val result = viewModel.getLocationSettings()
        MatcherAssert.assertThat(result, `is`("GPS"))
    }

    @Test
    fun putLocationSettingsAndRetrieve() {
        viewModel.putLocationSettings("manual")
        val result = viewModel.getLocationSettings()
        MatcherAssert.assertThat(result, `is`("manual"))
    }

}