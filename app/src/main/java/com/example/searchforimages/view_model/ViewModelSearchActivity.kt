package com.example.searchforimages.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

class ViewModelSearchActivity : ViewModel() {

    private val searchActivityStatus: MutableLiveData<SearchActivityStatus> = MutableLiveData()
    private val disposables: CompositeDisposable = CompositeDisposable()


    @Synchronized
    private fun addSub(disposable: Disposable) {
        disposables.let {
            disposables.add(disposable)
        }
    }

    override fun onCleared() {
        super.onCleared()
        if (!disposables.isDisposed) disposables.dispose()
    }

}