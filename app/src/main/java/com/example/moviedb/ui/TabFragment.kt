package com.example.moviedb.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.moviedb.databinding.FragmentTabParentBinding
import com.example.moviedb.data.entities.MovieCategory
import com.google.android.material.tabs.TabLayout

class TabFragment : Fragment() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = FragmentTabParentBinding.inflate(inflater, container, false)
        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)

        // Set up the ViewPager with the sections adapter.
        binding.container.adapter = mSectionsPagerAdapter

        binding.container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabs))
        binding.tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(binding.container))

        return binding.root
    }

    @SuppressLint("WrongConstant")
    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

        override fun getItem(position: Int): Fragment {
            return when(position){
                0 -> MoviesFragment.newInstance(MovieCategory.UPCOMING)
                1 -> MoviesFragment.newInstance(MovieCategory.POPULAR)
                else -> MoviesFragment.newInstance(MovieCategory.LATEST)
            }
        }

        override fun getCount(): Int {
            return 3
        }
    }
}
