package com.example.jawwy.Favourites

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.jawwy.favourites.FavouriteApiState
import com.example.jawwy.favourites.viewmodel.FavouriteViewModel
import com.example.jawwy.model.data.JsonPojo
import com.example.jawwy.model.repo.IWeatherRepository
import getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.Closeable

@Config(manifest=Config.NONE)
@RunWith(AndroidJUnit4::class)
class FavouriteViewModelTest {
    @get:Rule
    val rule = InstantTaskExecutorRule()
    var mainCoroutineRule = MainCoroutineRule()
    lateinit var repo: IWeatherRepository
    lateinit var viewModel: FavouriteViewModel
    lateinit var weatherListStateFlow:MutableStateFlow<FavouriteApiState>
    private lateinit var testDispatcher: TestCoroutineDispatcher
    private lateinit var mainThreadSurrogate: Closeable
    @Before
    fun setup(){
        weatherListStateFlow = MutableStateFlow(FavouriteApiState.Loading)
        repo= FakeRepository()
        testDispatcher= TestCoroutineDispatcher()
        viewModel=FavouriteViewModel(repo,testDispatcher)
    }


    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getAllWeather_weatherListUpdated()= mainCoroutineRule.runBlockingTest {
        // Trigger the ViewModel method
        viewModel.getAllWeather()

        // Simulate data emission from repository
        weatherListStateFlow.value = FavouriteApiState.Success(arrayListOf())

        // Await the StateFlow update
        val result = viewModel.weatherList.first()

        // Assert the StateFlow value
        assertThat(result, CoreMatchers.instanceOf(FavouriteApiState.Success::class.java))
    }
    @Test
    fun deleteFav_deleteResultReturned()=mainCoroutineRule.runBlockingTest {
        viewModel.deletefav(JsonPojo(id = "1", gps = "no"))

        //then
        val result = viewModel.deleteResult.getOrAwaitValue {  }
        assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
    }



//    @Test
//    fun setFilter_allFilterTask_allLabelsCurrentFilteringLabelNoTasksAllNoTaskLabelLogoNoFillNoTaskIconResTasksAddViewVisible() {
//        //when
//        viewModel.setFiltering(TasksFilterType.ALL_TASKS)
//        //then
//        val result1 = viewModel.currentFilteringLabel.getOrAwaitValue {  }
//        val result2 = viewModel.noTasksLabel.getOrAwaitValue {  }
//        val result3 = viewModel.noTaskIconRes.getOrAwaitValue {  }
//        val result4 = viewModel.tasksAddViewVisible.getOrAwaitValue {  }
//
//
//        MatcherAssert.assertThat(result1, CoreMatchers.`is`(R.string.label_all))
//        MatcherAssert.assertThat(result2, CoreMatchers.`is`(R.string.no_tasks_all))
//        MatcherAssert.assertThat(result3, CoreMatchers.`is`(R.drawable.logo_no_fill))
//        MatcherAssert.assertThat(result4, CoreMatchers.`is`(true))
//
//
//
//    }
//    @Test
//    fun addNewTask_newTaskEventNotNull(){
//        //when
//        viewModel.addNewTask()
//        //then
//        val result = viewModel.newTaskEvent.getOrAwaitValue {  }
//        MatcherAssert.assertThat(result, CoreMatchers.not(CoreMatchers.nullValue()))
//    }
//
//    @Test
//    fun completeTask_dataAndSnackbarUpdated() = mainCoroutineRule.runBlockingTest {
//        val result = viewModel.completeTask(Task(isCompleted = true),true)
//        MatcherAssert.assertThat(result.isCompleted, CoreMatchers.`is`(true))
//
//    }
}