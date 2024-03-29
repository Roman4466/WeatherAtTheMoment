package com.example.weatherNow.features.history

import androidx.lifecycle.Observer
import com.example.weatherNow.base.BaseViewModel
import com.example.weatherNow.model.Repository
import com.example.weatherNow.model.entity.db.WeatherEntity

class HistoryViewModel(
    private val repository: Repository = Repository
) : BaseViewModel<List<WeatherEntity>?, HistoryViewState>() {

    private val notesObserver = Observer<List<WeatherEntity>> {
        if (it == null) return@Observer

        viewStateLiveData.value = HistoryViewState(weather = it)
    }

    private val repositoryNotes = repository.getWeathers()

    init {
        viewStateLiveData.value = HistoryViewState()
        repositoryNotes.observeForever(notesObserver)
    }

    override fun onCleared() {
        repositoryNotes.removeObserver(notesObserver)
    }

    fun deleteForId(id: Long){
        repository.deleteWeatherById(id)
    }

    fun deleteAll(){
        repository.deleteAll()
    }

    fun search(str: String){
        viewStateLiveData.value = HistoryViewState(repository.search(str).value)
    }

    fun sort(type : Int) {
        viewStateLiveData.value = HistoryViewState(weather = when(type){
            Sortings.DATE -> repository.getAllSortedByDate(1).value
            Sortings.DATEDESC -> repository.getAllSortedByDate(2).value
            Sortings.NAME -> repository.getAllSortedByName(1).value
            Sortings.NAMEDESC -> repository.getAllSortedByName(2).value
            else -> null
        })
    }
}