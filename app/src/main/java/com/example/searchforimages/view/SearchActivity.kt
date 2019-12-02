package com.example.searchforimages.view

import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchforimages.R
import com.example.searchforimages.utils.LoadMoreRecyclerViewListener
import com.example.searchforimages.view_model.SearchActivityStatus
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
        initSearchViews()
    }

    private fun initSearchViews() {
        etSearch.addTextChangedListener {
            viewModelSearchActivity.search(it.toString())
        }
    }

    private fun initViewModelSubscriptions() {
        viewModelSearchActivity.searchActivityStatus.observe(this, Observer {
            it?.let {
                updateSearchStatus(it)
            }
        })
    }

    private fun updateSearchStatus(searchActivityStatus: SearchActivityStatus) {
        Toast.makeText(this, searchActivityStatus.status.name, Toast.LENGTH_LONG).show();
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerImages.layoutManager = linearLayoutManager
        //todo: set adapter to recycler view
        recyclerImages.addOnScrollListener(object :
            LoadMoreRecyclerViewListener(linearLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                viewModelSearchActivity.loadMore(etSearch.text.toString())
            }
        })
    }

    private fun intViewModel() {
        viewModelSearchActivity =
            ViewModelProviders.of(this).get(ViewModelSearchActivity::class.java)
    }
}
