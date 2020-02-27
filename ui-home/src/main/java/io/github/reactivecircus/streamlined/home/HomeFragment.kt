package io.github.reactivecircus.streamlined.home

import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.home.databinding.FragmentHomeBinding
import io.github.reactivecircus.streamlined.navigator.NavigatorProvider
import io.github.reactivecircus.streamlined.ui.base.BaseFragment
import io.github.reactivecircus.streamlined.ui.configs.AnimationConfigs
import io.github.reactivecircus.streamlined.ui.viewmodel.fragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import reactivecircus.flowbinding.swiperefreshlayout.refreshes
import javax.inject.Inject
import javax.inject.Provider

@OptIn(ExperimentalCoroutinesApi::class)
class HomeFragment @Inject constructor(
        analyticsApi: AnalyticsApi,
        private val navigatorProvider: NavigatorProvider,
        private val viewModelProvider: Provider<HomeViewModel>,
        private val animationConfigs: AnimationConfigs
) : BaseFragment(R.layout.fragment_home, analyticsApi) {

    private val viewModel: HomeViewModel by fragmentViewModel { viewModelProvider.get() }

    private lateinit var homeFeedsListAdapter: HomeFeedsListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)

        binding.toolbar.title = getString(R.string.title_home)

        binding.swipeRefreshLayout.refreshes()
                .onEach { viewModel.refreshHomeFeeds() }
                .launchIn(lifecycleScope)

        homeFeedsListAdapter = HomeFeedsListAdapter(
                actionListener = actionListener,
                animationConfigs = if (savedInstanceState == null) animationConfigs else null
        )

        binding.homeFeedsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = homeFeedsListAdapter
        }

        viewModel.state.observe<HomeState>(viewLifecycleOwner) {
            homeFeedsListAdapter.submitList(it.items)
        }
    }

    private val actionListener = object : HomeFeedsListAdapter.ActionListener {
        override fun storyClicked(story: Story) {
            // TODO
        }

        override fun bookmarkToggled(story: Story) {
            // TODO
        }

        override fun moreButtonClicked(story: Story) {
            // TODO
        }

        override fun readMoreHeadlinesButtonClicked() {
            navigatorProvider.get().navigateToHeadlinesScreen()
        }
    }
}
