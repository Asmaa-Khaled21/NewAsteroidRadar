package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Repositry.Repo
import com.udacity.asteroidradar.api.Utils
import com.udacity.asteroidradar.room.AsteroidData
import kotlinx.coroutines.launch

class MainViewModel(app: Application) : ViewModel() {

    private val database = AsteroidData.getDatabase(app)
    private val repositoryAstroid= Repo(database)


    private val asteroidFilter = MutableLiveData(Utils.AsteroidFilter.SHOW_SAVED)

    private val repo = Repo(database)
    init {
        viewModelScope.launch {
            repo.refresh()
        }
    }

    private val _asteroidSelected = MutableLiveData<Asteroid?>()
    val selectedProperty: MutableLiveData<Asteroid?>
        get() = _asteroidSelected
    fun displayAsteroid() {
        _asteroidSelected.value = null
    }

    val imageOfDay = repo.pictureOfDay
    val asteroids = Transformations.switchMap(asteroidFilter){
        when (it) {
            Utils.AsteroidFilter.SHOW_TODAY -> repositoryAstroid.asteroidToday
            Utils.AsteroidFilter.SHOW_WEEK -> repositoryAstroid.asteroidWeek
            else -> repositoryAstroid.asteroidAll
        }
    }
    fun applyFilter(selectedDateItem: Utils.AsteroidFilter) {
        asteroidFilter.value=selectedDateItem } }

//    Factory for constructing MainViewModel with parameter
class Factory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}