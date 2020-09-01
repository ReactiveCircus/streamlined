package io.github.reactivecircus.streamlined.headlines

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.streamlined.ui.ScreenForAnalytics
import javax.inject.Inject

@AndroidEntryPoint
class HeadlinesFragment @Inject constructor() : Fragment(), ScreenForAnalytics {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(context = requireContext()).apply {
            setContent {
                HeadlinesScreen()
            }
        }
    }
}
