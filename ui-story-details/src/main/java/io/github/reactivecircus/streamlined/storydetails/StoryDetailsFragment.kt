package io.github.reactivecircus.streamlined.storydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.storydetails.databinding.FragmentStoryDetailsBinding
import io.github.reactivecircus.streamlined.ui.base.BaseFragment
import io.github.reactivecircus.streamlined.ui.viewmodel.fragmentViewModel
import javax.inject.Inject
import javax.inject.Provider

class StoryDetailsFragment @Inject constructor(
    analyticsApi: AnalyticsApi,
    private val viewModelProvider: Provider<StoryDetailsViewModel.Factory>
) : BaseFragment(analyticsApi) {

    private val binding get() = view?.tag as FragmentStoryDetailsBinding

    private val viewModel: StoryDetailsViewModel by fragmentViewModel {
        val id = requireArguments().getLong(ARG_STORY_ID)
        viewModelProvider.get().create(id)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentStoryDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        view.tag = binding
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = "Story title..."
    }

    companion object {
        const val ARG_STORY_ID = "ARG_STORY_ID"
    }
}
