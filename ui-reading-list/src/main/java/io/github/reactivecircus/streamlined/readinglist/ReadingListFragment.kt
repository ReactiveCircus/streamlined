package io.github.reactivecircus.streamlined.readinglist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.github.reactivecircus.streamlined.navigator.NavigatorProvider
import io.github.reactivecircus.streamlined.readinglist.databinding.FragmentReadingListBinding
import io.github.reactivecircus.streamlined.ui.Screen
import javax.inject.Inject

class ReadingListFragment @Inject constructor(
    private val navigatorProvider: NavigatorProvider
) : Fragment(R.layout.fragment_reading_list), Screen {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentReadingListBinding.bind(view)

        binding.toolbar.title = getString(R.string.title_reading_list)
    }
}
