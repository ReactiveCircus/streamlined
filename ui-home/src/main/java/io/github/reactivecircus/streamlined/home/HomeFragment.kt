package io.github.reactivecircus.streamlined.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.observe
import com.dropbox.android.external.store4.StoreResponse
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.home.databinding.FragmentHomeBinding
import io.github.reactivecircus.streamlined.navigator.NavigatorProvider
import io.github.reactivecircus.streamlined.ui.base.BaseFragment
import io.github.reactivecircus.streamlined.ui.viewmodel.fragmentViewModel
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Provider

class HomeFragment @Inject constructor(
    analyticsApi: AnalyticsApi,
    private val navigatorProvider: NavigatorProvider,
    private val viewModelProvider: Provider<HomeViewModel>
) : BaseFragment(R.layout.fragment_home, analyticsApi) {

    private val viewModel: HomeViewModel by fragmentViewModel { viewModelProvider.get() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)

        binding.toolbar.title = getString(R.string.title_home)

        viewModel.state.observe<StoreResponse<List<Story>>>(this) {
            Timber.d(it.toString())
        }
    }
}
