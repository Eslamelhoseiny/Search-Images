package com.example.searchforimages.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.searchforimages.dagger.component.DaggerApplicationGraph
import com.example.searchforimages.model.ImagePost
import com.example.searchforimages.model.Response
import com.example.searchforimages.repository.ImageRepository
import com.example.searchforimages.utils.extension.isValidURL
import com.example.searchforimages.view.adapter.ImagePostAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

class ViewModelSearchActivity : ViewModel() {

    val searchActivityStatus: MutableLiveData<SearchActivityStatus> = MutableLiveData()
    private val disposables: CompositeDisposable = CompositeDisposable()
    private lateinit var searchEmitter: PublishSubject<String>
    var imageRepository: ImageRepository =
        DaggerApplicationGraph.builder().build().getImageRepository()
    private var currentPage: Int = 1
    val adapter = ImagePostAdapter(emptyList())

    init {
        initSearch()
    }

    fun search(query: String) {
        currentPage = 1
        adapter.resetItems()
        searchEmitter.onNext(query)
    }

    private fun initSearch() {
        searchEmitter = PublishSubject.create<String>()
        addDisposable(
            searchEmitter
                .subscribeOn(Schedulers.io())
                .debounce(500, TimeUnit.MILLISECONDS)
                .doOnNext {
                    if (it.length < 2) {
                        searchActivityStatus.postValue(SearchActivityStatus.tooShort())
                    }
                }
                .filter { it.length > 2 }
                .doOnNext { searchActivityStatus.postValue(SearchActivityStatus.loading()) }
                .doOnTerminate { searchActivityStatus.postValue(SearchActivityStatus.complete()) }
                .switchMap {
                    searchForImages(it)
                }
                .map {
                    val newData = Response(it.data?.filter { item ->
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
                        initSearch()
                        searchActivityStatus.postValue(SearchActivityStatus.error(it))
                    })
        )
    }

    private fun updateAdapter(items: List<ImagePost>?) {
        items?.let {
            if (adapter.getImagesList().isNullOrEmpty()) {
                adapter.setImages(it)
            } else {
                adapter.addImages(it)
            }
        }
    }

    fun loadMore(searchQuery: String, currentPage: Int) {
        this.currentPage = currentPage
        searchEmitter.onNext(searchQuery)
    }

    private fun searchForImages(query: String): Observable<Response> {
        return imageRepository.getImagePosts(currentPage, query).toObservable()
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