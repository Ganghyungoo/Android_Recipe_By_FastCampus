package com.test.architecturepatternproject.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.test.architecturepatternproject.mvvm.model.Image
import com.test.architecturepatternproject.mvvm.repository.ImageRepository
import io.reactivex.disposables.CompositeDisposable

class MvvmViewModel(private val imageRepository: ImageRepository) : ViewModel() {
    private val _countLiveData = MutableLiveData<String>()
    val countLiveData:LiveData<String> by lazy { _countLiveData }

    private val _imageLiveData = MutableLiveData<Image>()
    val imageLiveData:LiveData<Image> by lazy { _imageLiveData }

    //xJava에서 사용되는 Disposable 객체들을 관리하기 위한 컨테이너입니다. Disposable은 Observable이나 Flowable을 구독할 때 반환되는 객체로, 해당 구독을 해지할 때 사용됩니다
    private var disposable: CompositeDisposable? = CompositeDisposable()
    private var count = 0

    fun loadRandomImage() {
        disposable?.add(imageRepository.getRandomImage()
            .doOnSuccess {
                count++
            }
            .subscribe { item ->
                _imageLiveData.value = item
                _countLiveData.value = "불러온 이미지 수: $count"
            })
    }

    override fun onCleared() {
        super.onCleared()
        disposable = null
    }

    //ViewModel의 인스턴스를 생성하기 위한 Factory 클래스입니다. 안드로이드에서는 ViewModel을 생성할 때 ViewModel에 대한 의존성을 주입하기 위해 Factory를 사용하는 것이 권장됩니다
    class MvvmViewModelFactory(private val imageRepository: ImageRepository) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MvvmViewModel(imageRepository) as T
        }
    }
}