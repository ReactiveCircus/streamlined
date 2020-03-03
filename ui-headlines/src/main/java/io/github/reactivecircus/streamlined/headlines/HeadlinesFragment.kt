package io.github.reactivecircus.streamlined.headlines

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.github.reactivecircus.streamlined.headlines.databinding.FragmentHeadlinesBinding
import io.github.reactivecircus.streamlined.navigator.NavigatorProvider
import io.github.reactivecircus.streamlined.ui.Screen
import javax.inject.Inject

class HeadlinesFragment @Inject constructor(
    private val navigatorProvider: NavigatorProvider
) : Fragment(R.layout.fragment_headlines), Screen {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHeadlinesBinding.bind(view)

        binding.toolbar.title = getString(R.string.title_headlines)
    }
}
