package com.example.searchforimages.view.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

@SuppressLint("Registered")
open class BaseActivity : AppCompatActivity() {

    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        compositeDisposable = CompositeDisposable()
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }
}
