package com.test.searchmediaproject.ui.display_search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.searchmediaproject.Common
import com.test.searchmediaproject.model.ImageItem
import com.test.searchmediaproject.model.ListItem
import com.test.searchmediaproject.model.VideoItem
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
    fun onclickFavoriteToggle(item:ListItem) {
        _listLiveData.value = _listLiveData.value?.map {
            if(it == item){
                when(it){
                    is ImageItem -> {
                        it.copy(isFavorite = !it.isFavorite)
                    }
                    is VideoItem -> {
                        it.copy(isFavorite = !it.isFavorite)
                    }
                    else -> {
                        it
                    }
                }.also {changeItem ->
                    if (Common.favoritesList.contains(item)){
                        Common.favoritesList.remove(item)
                    }else{
                        Common.favoritesList.add(changeItem)
                    }
                }
            }else{
                it
            }
        }
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