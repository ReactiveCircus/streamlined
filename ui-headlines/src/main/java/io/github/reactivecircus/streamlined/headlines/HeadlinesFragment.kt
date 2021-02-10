package io.github.reactivecircus.streamlined.headlines

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.streamlined.headlines.databinding.FragmentHeadlinesBinding
import io.github.reactivecircus.streamlined.ui.ScreenForAnalytics

@AndroidEntryPoint
class HeadlinesFragment : Fragment(R.layout.fragment_headlines), ScreenForAnalytics {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentHeadlinesBinding.bind(view)

        binding.toolbar.title = getString(R.string.title_headlines)
    }
}
