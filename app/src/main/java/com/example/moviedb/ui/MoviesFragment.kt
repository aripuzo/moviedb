package com.example.moviedb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.moviedb.databinding.FragmentListBinding
import com.example.moviedb.data.entities.MovieCategory
import com.example.moviedb.utils.Status
import com.example.moviedb.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesFragment : Fragment() {

    private val viewModel by viewModels<MovieViewModel> ()

    private lateinit var binding: FragmentListBinding

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
        binding = FragmentListBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MoviesFragment
            viewmodel = viewModel
            type = this.type
        }
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.salesList.layoutManager = staggeredGridLayoutManager
        binding.salesList.setHasFixedSize(true)

        listAdapter = MoviesAdapter()
        listAdapter.setClickListener {
            findNavController().navigate(
                SearchFragmentDirections.actionSearchFragmentToMovieDetail(it)
            )
        }
        binding.salesList.adapter = listAdapter
        setupObserver()
        coroutineScope.launch(Dispatchers.IO) {
            getMovies()
        }
        binding.retryBtn.setOnClickListener {
            getMovies()
        }
        return binding.root
    }

    private fun getMovies() = viewModel.getMovies(movieCategory)

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
            MoviesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("category", category)
                }
            }
    }
}
