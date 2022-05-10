package com.example.movieapp.ui.movie

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.example.movieapp.R
import com.example.movieapp.core.Resource
import com.example.movieapp.data.model.Movie
import com.example.movieapp.data.remote.RemoteMovieDataSource
import com.example.movieapp.databinding.FragmentMovieBinding
import com.example.movieapp.presentation.MovieViewModel
import com.example.movieapp.presentation.MovieViewModelFactory
import com.example.movieapp.repository.MovieRepositoryImpl
import com.example.movieapp.repository.RetrofitClient
import com.example.movieapp.ui.movie.adapters.MovieAdapter
import com.example.movieapp.ui.movie.adapters.concat.PopularConcatAdapter
import com.example.movieapp.ui.movie.adapters.concat.TopRatedConcatAdapter
import com.example.movieapp.ui.movie.adapters.concat.UpcomingConcatAdapter

class MovieFragment : Fragment(R.layout.fragment_movie), MovieAdapter.OnMovieClickListener {

    private lateinit var binding: FragmentMovieBinding
    private val viewModel by viewModels<MovieViewModel> {// by indica delegación para crear la instancia
        MovieViewModelFactory(MovieRepositoryImpl(RemoteMovieDataSource(RetrofitClient.webService))) // Inyección de dependencias manual
    }

    private lateinit var concatAdapter: ConcatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieBinding.bind(view)

        concatAdapter = ConcatAdapter()

        viewModel.fetchMainScreenMovies().observe( viewLifecycleOwner, Observer { result ->
            when(result) {
                is Resource.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    concatAdapter.apply {
                        addAdapter(0, UpcomingConcatAdapter(MovieAdapter(result.data.first.results, this@MovieFragment)))
                        addAdapter(0, TopRatedConcatAdapter(MovieAdapter(result.data.second.results, this@MovieFragment)))
                        addAdapter(0, PopularConcatAdapter(MovieAdapter(result.data.third.results, this@MovieFragment)))
                    }
                    Log.d("LiveData", "Upcoming: ${result.data.first}")
                    Log.d("LiveData", "TopRated: ${result.data.second}")
                    Log.d("LiveData", "Popular: ${result.data.third}")

                    binding.rvMovies.adapter = concatAdapter
                }
                is Resource.Failure -> {
                    Log.d("Error", "${result.exception}")
                }
            }
        })
    }

    override fun onMovieClick(movie: Movie) {
        val action = MovieFragmentDirections.actionMovieFragmentToMovieDetailFragment(
            movie.poster_path,
            movie.backdrop_path,
            movie.vote_average.toFloat(),
            movie.vote_count,
            movie.overview,
            movie.title,
            movie.original_language,
            movie.release_date
        )
        findNavController().navigate(action)
    }
}