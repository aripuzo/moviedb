package com.decagonhq.moviedb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.decagonhq.moviedb.App
import com.decagonhq.moviedb.databinding.FragmentListBinding
import com.decagonhq.moviedb.viewmodel.MovieViewModel
import com.decagonhq.moviedb.viewmodel.MovieViewModelFactory
import kotlinx.coroutines.*

class MoviesFragment : Fragment() {

    private val viewModel by viewModels<MovieViewModel> {
        MovieViewModelFactory((requireContext().applicationContext as App).taskRepository)
    }

    private lateinit var viewDataBinding: FragmentListBinding

    private lateinit var listAdapter: MoviesAdapter

    var type = 0

    private val job = Job()

    private val coroutineScope =
        CoroutineScope(Dispatchers.Main + job + coroutineExceptionHandler)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt("type")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentListBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
            type = this.type
        }
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2,
            StaggeredGridLayoutManager.VERTICAL)
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        viewDataBinding.salesList.layoutManager = staggeredGridLayoutManager
        viewDataBinding.salesList.setHasFixedSize(true)
        viewDataBinding.salesList.adapter = listAdapter
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Set the lifecycle owner to the lifecycle of the view
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner
        if(type == 0){
            coroutineScope.launch(Dispatchers.IO) {
                viewModel.getPopularMovies()
            }
        }else{
            viewModel.getFavoriteMovies()
        }
    }

    private val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            coroutineScope.launch(Dispatchers.Main) {
                //errorMessage.visibility = View.VISIBLE
                //errorMessage.text = getString(R.string.error_message)
            }

            GlobalScope.launch { println("Caught $throwable") }
        }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        @JvmStatic
        fun newInstance(type: Int) =
            MoviesFragment().apply {
                arguments = Bundle().apply {
                    putInt("type", type)
                }
            }
    }
}
