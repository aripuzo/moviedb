package com.example.moviedb.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.moviedb.databinding.FragmentMovieDetailBinding
import com.example.moviedb.viewmodel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {

    private val viewModel by viewModels<MovieDetailViewModel>()
    private lateinit var binding: FragmentMovieDetailBinding
    private val args: MovieDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = this@MovieDetailFragment
            viewModel = this@MovieDetailFragment.viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFavorite.setOnClickListener {
            viewModel.favoriteOrUnFavoriteMovie()
        }
        viewModel.apply {
            args.movie.let {
                movie.postValue(it)
                checkFavorite(it.id)
            }
        }
        setupObserver()
    }

    private fun setupObserver() {
        viewModel.remoteResponse.observe(viewLifecycleOwner) {
            it?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }
}