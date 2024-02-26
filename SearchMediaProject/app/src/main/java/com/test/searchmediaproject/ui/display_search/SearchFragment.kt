package com.test.searchmediaproject.ui.display_search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.test.searchmediaproject.databinding.FragmentSearchBinding
import com.test.searchmediaproject.list.ItemHandler
import com.test.searchmediaproject.list.ListAdapter
import com.test.searchmediaproject.model.ListItem
import com.test.searchmediaproject.network.RetrofitManager
import com.test.searchmediaproject.repository.SearchRepositoryImpl

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.SearchViewModelFactory(SearchRepositoryImpl(RetrofitManager.searchService))
    }
    private var fragmentSearchBinding: FragmentSearchBinding? = null

    private val adapter by lazy { ListAdapter(Handler(viewModel)) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return FragmentSearchBinding.inflate(inflater, container, false).apply {
            fragmentSearchBinding = this
            lifecycleOwner = viewLifecycleOwner
            viewModel = this@SearchFragment.viewModel
        }.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentSearchBinding?.apply {
            recyclerView.adapter = adapter
        }
        observeViewModel()
    }

    fun searchKeyword(text: String) {
        viewModel.search(text)
    }

    fun observeViewModel() {
        viewModel.listLiveData.observe(viewLifecycleOwner) { list ->
            fragmentSearchBinding?.let {
                if (list.isEmpty()) {
                    it.emptyTextView.isVisible = true
                    it.recyclerView.isVisible = false
                } else {
                    it.emptyTextView.isVisible = false
                    it.recyclerView.isVisible = true
                }
            }
            Log.d("testt", "$list")
            adapter.submitList(list)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentSearchBinding = null
    }

    class Handler(private val viewModel: SearchViewModel):ItemHandler {
        override fun onClickFavorite(item: ListItem) {
            viewModel.onclickFavoriteToggle(item)
        }
    }
}