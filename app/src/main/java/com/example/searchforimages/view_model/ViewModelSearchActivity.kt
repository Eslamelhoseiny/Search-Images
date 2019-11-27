package com.example.searchforimages.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.searchforimages.model.ImgurResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ViewModelSearchActivity : ViewModel() {

    private val searchActivityStatus: MutableLiveData<SearchActivityStatus> = MutableLiveData()
    private val disposables: CompositeDisposable = CompositeDisposable()
    private lateinit var searchEmitter: PublishSubject<String>

    init {
        initSearch()
    }

    private fun initSearch() {
        searchEmitter = PublishSubject.create<String>()
        addDisposable(
            searchEmitter
                .subscribeOn(Schedulers.io())
                .debounce(250, TimeUnit.MILLISECONDS)
                .doOnNext {
                    if (it.length < 2) {
                        searchActivityStatus.postValue(SearchActivityStatus.tooShort())
                    }
                }
                .filter { it.length > 1 }
                .doOnNext { searchActivityStatus.postValue(SearchActivityStatus.loading()) }
                .doOnTerminate { searchActivityStatus.postValue(SearchActivityStatus.complete()) }
                .switchMap {
                    searchForImages(it).toObservable()
                }
                .map {
                    val newData = ImgurResponse(it.data?.filter { item ->
                        item.images?.isNotEmpty() == true && item.images.first().link.isValidURL()
                                && item.nsfw == false
                                && item.images.first().size ?: Long.MAX_VALUE <= 1500000L
                    })
                    newData
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext { response ->
                    updateAdapter(response.data)
                    searchActivityStatus.postValue(SearchActivityStatus.success())
                }
                .subscribe({},
                    {
                        searchActivityStatus.postValue(SearchActivityStatus.error(it))
                    })
        )
    }


    @Synchronized
    private fun addDisposable(disposable: Disposable) {
        disposables.let {
            disposables.add(disposable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposables.isDisposed) disposables.dispose()
    }

}