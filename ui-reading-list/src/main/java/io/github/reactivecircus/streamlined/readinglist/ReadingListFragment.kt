package io.github.reactivecircus.streamlined.readinglist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.streamlined.readinglist.databinding.FragmentReadingListBinding
import io.github.reactivecircus.streamlined.ui.ScreenForAnalytics

@AndroidEntryPoint
class ReadingListFragment : Fragment(R.layout.fragment_reading_list), ScreenForAnalytics {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentReadingListBinding.bind(view)

        binding.toolbar.title = getString(R.string.title_reading_list)
    }
}
