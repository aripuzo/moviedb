package com.example.moviedb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moviedb.App
import com.example.moviedb.databinding.FragmentSearchListBinding
import com.example.moviedb.domain.entities.MovieCategory
import com.example.moviedb.utils.Status
import com.example.moviedb.viewmodel.MovieViewModel
import com.example.moviedb.viewmodel.MovieViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private val viewModel by viewModels<MovieViewModel> {
        MovieViewModelFactory((requireContext().applicationContext as App).taskRepository, movieCategory)
    }

    private lateinit var binding: FragmentSearchListBinding

    private lateinit var listAdapter: MoviesAdapter

    var movieCategory = MovieCategory.LATEST

    private val job = Job()

    private val coroutineScope =
        CoroutineScope(Dispatchers.Main + job)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieCategory = it.getSerializable("category") as MovieCategory
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchListBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            type = this.type
        }
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.salesList.layoutManager = staggeredGridLayoutManager
        binding.salesList.setHasFixedSize(true)

        listAdapter = MoviesAdapter()
        binding.salesList.adapter = listAdapter
        setupObserver()
        binding.txtSearch.doOnTextChanged { text, start, count, after ->
            if (!text.isNullOrEmpty() && text.length > 2) {
                searchMovies(text.toString())
            }// you can do anything
        }

        binding.txtSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (!binding.txtSearch.text.isNullOrEmpty()) {
                    searchMovies(binding.txtSearch.text.toString())
                }// you can do anything
                return@setOnEditorActionListener true
            }
            false
        }
        binding.txtSearch
        return binding.root
    }

    private fun searchMovies(query: String) = viewModel.searchMovies(query)

    private fun setupObserver() {
        viewModel.moviesResponse?.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }

        viewModel.remoteResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    if (listAdapter.itemCount == 0) {
                        binding.progressCircular.isVisible = true
                        binding.noContent.isVisible = false
                    }
                }
                Status.SUCCESS -> {
                    binding.progressCircular.isVisible = false
                    binding.noContent.isVisible = false
                    binding.salesList.isVisible = true
                    listAdapter.submitList(it.data?.results)
                }
                Status.ERROR -> {
                    if (listAdapter.itemCount == 0) {
                        binding.progressCircular.isVisible = false
                        binding.noContent.isVisible = true
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        @JvmStatic
        fun newInstance(category: MovieCategory) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("category", category)
                }
            }
    }
}
