package com.example.searchforimages.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchforimages.R
import com.example.searchforimages.utils.LoadMoreRecyclerViewListener
import com.example.searchforimages.view_model.ViewModelSearchActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {

    private lateinit var viewModelSearchActivity: ViewModelSearchActivity
    private lateinit var loadMoreRecyclerViewListener: LoadMoreRecyclerViewListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        init()
    }

    private fun init() {
        intViewModel()
        initRecyclerView()
        initViewModelSubscriptions()
    }

    private fun initViewModelSubscriptions() {
        //todo subscribe on view model loading state
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerImages.layoutManager = linearLayoutManager
        //todo: set adapter to recycler view
        recyclerImages.addOnScrollListener(object :
            LoadMoreRecyclerViewListener(linearLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                //todo load more from view holder
            }
        })
    }

    private fun intViewModel() {
        viewModelSearchActivity =
            ViewModelProviders.of(this).get(ViewModelSearchActivity::class.java)
    }
}
