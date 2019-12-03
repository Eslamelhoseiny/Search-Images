package com.example.searchforimages.view.activity

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.searchforimages.R
import com.example.searchforimages.utils.LoadMoreRecyclerViewListener
import com.example.searchforimages.utils.Status
import com.example.searchforimages.utils.extension.showToast
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
            loadMoreRecyclerViewListener.reset()
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
        showToast(searchActivityStatus.status.name)
        when (searchActivityStatus.status) {
            Status.LOADING -> {
//                progress_spinner.visibility = View.VISIBLE
//                search_result_header.visibility = View.GONE
//                upward_arrow.visibility = View.GONE
//                instructions.visibility = View.GONE
            }

            Status.TOO_SHORT -> {
//                upward_arrow.visibility = View.VISIBLE
//                instructions.visibility = View.VISIBLE
//                search_result_header.visibility = View.GONE
            }

            Status.SUCCESS -> {
//                if (image_post_list.adapter.itemCount == 0) {
//                    toastLong(R.string.no_images)
//                    upward_arrow.visibility = View.VISIBLE
//                    instructions.visibility = View.VISIBLE
//                    search_result_header.visibility = View.GONE
//                } else {
//                    search_result_header.visibility = View.VISIBLE
//                    search_result_header.text =
//                        resources.getString(R.string.search_result_header, searchView?.query)
//                }
                loadMoreRecyclerViewListener.setLoading(false)
//                progress_spinner.visibility = View.GONE
            }

            Status.COMPLETE -> {
//                progress_spinner.visibility = View.GONE
            }

            Status.ERROR -> {
//                Log.e("MainActivity: ", state.error?.localizedMessage)
            }
        }
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerImages.layoutManager = linearLayoutManager
        recyclerImages.adapter = viewModelSearchActivity.adapter
        loadMoreRecyclerViewListener = object : LoadMoreRecyclerViewListener(linearLayoutManager) {
            override fun onLoadMore(currentPage: Int) {
                viewModelSearchActivity.loadMore(etSearch.text.toString(), currentPage)
            }
        }
        recyclerImages.addOnScrollListener(loadMoreRecyclerViewListener)
    }

    private fun intViewModel() {
        viewModelSearchActivity =
            ViewModelProviders.of(this).get(ViewModelSearchActivity::class.java)
    }
}
