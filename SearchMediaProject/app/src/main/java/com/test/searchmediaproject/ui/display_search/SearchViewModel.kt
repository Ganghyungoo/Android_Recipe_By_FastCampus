package com.test.searchmediaproject.ui.display_search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.searchmediaproject.model.ListItem
import com.test.searchmediaproject.repository.SearchRepository
import io.reactivex.rxjava3.disposables.CompositeDisposable

class SearchViewModel(private val searchRepository: SearchRepository):ViewModel() {

    private val _listLiveData = MutableLiveData<List<ListItem>>()
    val listLiveData:LiveData<List<ListItem>> get() = _listLiveData

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading:LiveData<Boolean> get() = _showLoading

    private var disposable: CompositeDisposable? = CompositeDisposable()

    fun search(query: String){
        disposable?.add(searchRepository.search(query)
            .doOnSubscribe {_showLoading.value = true}
            .doOnTerminate {_showLoading.value = false}
            .subscribe ({ list->
                _listLiveData.value = list
                Log.d("testt","${_listLiveData.value}")
            },{
                _listLiveData.value = emptyList()
            })
        )
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
        disposable = null
    }


    class SearchViewModelFactory(private val searchRepository:SearchRepository):ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SearchViewModel(searchRepository) as T
        }
    }
}