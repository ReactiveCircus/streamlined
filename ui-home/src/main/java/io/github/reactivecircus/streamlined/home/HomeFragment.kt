package io.github.reactivecircus.streamlined.home

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import io.github.reactivecircus.streamlined.design.setDefaultBackgroundColor
import io.github.reactivecircus.streamlined.home.databinding.FragmentHomeBinding
import io.github.reactivecircus.streamlined.navigator.NavControllerType
import io.github.reactivecircus.streamlined.navigator.input.StoryDetailsInput
import io.github.reactivecircus.streamlined.navigator.navigate
import io.github.reactivecircus.streamlined.ui.ScreenForAnalytics
import io.github.reactivecircus.streamlined.ui.util.ItemActionListener
import io.github.reactivecircus.streamlined.ui.util.disableItemAnimatorIfTurnedOffGlobally
import io.github.reactivecircus.streamlined.ui.viewmodel.fragmentViewModel
import javax.inject.Inject
import javax.inject.Provider
import kotlinx.coroutines.flow.collect
import io.github.reactivecircus.streamlined.navigator.R as NavigatorResource
import io.github.reactivecircus.streamlined.ui.R as CommonUiResource

class HomeFragment @Inject constructor(
    private val viewModelProvider: Provider<HomeViewModel>
) : Fragment(R.layout.fragment_home), ScreenForAnalytics {

    private val viewModel: HomeViewModel by fragmentViewModel { viewModelProvider.get() }

    private val itemActionListener = ItemActionListener<FeedsListAdapter.ItemAction> { action ->
        when (action) {
            is FeedsListAdapter.ItemAction.StoryClicked -> {
                navigate(
                    destination = NavigatorResource.id.openStoryDetailsScreen,
                    args = StoryDetailsInput(action.story.id),
                    navControllerType = NavControllerType.Root,
                )
            }
            is FeedsListAdapter.ItemAction.BookmarkToggled -> Unit
            is FeedsListAdapter.ItemAction.MoreButtonClicked -> Unit
            FeedsListAdapter.ItemAction.ReadMoreHeadlinesButtonClicked -> {
                navigate(NavigatorResource.id.headlinesScreen)
            }
        }
    }

    private var errorSnackbar: Snackbar? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentHomeBinding.bind(view)

        binding.toolbar.title = getString(R.string.title_home)

        val feedsListAdapter = FeedsListAdapter(viewLifecycleOwner, itemActionListener).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
        binding.recyclerView.apply {
            disableItemAnimatorIfTurnedOffGlobally()
            layoutManager = LinearLayoutManager(activity)
            adapter = feedsListAdapter
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.rendering.collect { rendering ->
                binding.swipeRefreshLayout.setOnRefreshListener { rendering.onRefresh() }
                binding.retryButton.setOnClickListener { rendering.onRefresh() }

                val state = rendering.state
                when (state) {
                    is HomeState.InFlight -> binding.showInFlightState(
                        hasContent = state.itemsOrNull != null
                    )
                    is HomeState.ShowingContent -> binding.showContentState()
                    is HomeState.Error.Permanent -> binding.showPermanentErrorState()
                    is HomeState.Error.Transient -> binding.showTransientErrorState()
                }
                state.itemsOrNull?.run {
                    feedsListAdapter.submitList(this)
                }
                if (binding.recyclerView.adapter == null) {
                    binding.recyclerView.adapter = feedsListAdapter
                }
            }
        }
    }

    override fun onDestroyView() {
        dismissErrorSnackbar()
        super.onDestroyView()
    }

    private fun FragmentHomeBinding.showContentState() {
        errorStateView.isVisible = false
        progressIndicator.isVisible = false
        swipeRefreshLayout.isRefreshing = false
        swipeRefreshLayout.isEnabled = true
        recyclerView.isVisible = true
        dismissErrorSnackbar()
    }

    private fun FragmentHomeBinding.showInFlightState(hasContent: Boolean) {
        errorStateView.isVisible = false
        progressIndicator.isVisible = !hasContent
        swipeRefreshLayout.isRefreshing = hasContent
        swipeRefreshLayout.isEnabled = hasContent
        recyclerView.isVisible = hasContent
        dismissErrorSnackbar()
    }

    private fun FragmentHomeBinding.showPermanentErrorState() {
        errorStateView.isVisible = true
        progressIndicator.isVisible = false
        swipeRefreshLayout.isRefreshing = false
        swipeRefreshLayout.isEnabled = false
        recyclerView.isVisible = false
        dismissErrorSnackbar()
    }

    private fun FragmentHomeBinding.showTransientErrorState() {
        errorStateView.isVisible = false
        progressIndicator.isVisible = false
        swipeRefreshLayout.isRefreshing = false
        swipeRefreshLayout.isEnabled = true
        recyclerView.isVisible = true
        val errorMessage = getString(
            CommonUiResource.string.error_message_could_not_refresh_content
        )
        errorSnackbar = Snackbar
            .make(root, errorMessage, Snackbar.LENGTH_INDEFINITE)
            .apply { setDefaultBackgroundColor() }
            .also { it.show() }
    }

    private fun dismissErrorSnackbar() {
        errorSnackbar?.dismiss()
        errorSnackbar = null
    }
}
