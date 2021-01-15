package io.github.reactivecircus.streamlined.storydetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.MaterialElevationScale
import io.github.reactivecircus.streamlined.navigator.input.StoryDetailsInput
import io.github.reactivecircus.streamlined.navigator.requireNavInput
import io.github.reactivecircus.streamlined.storydetails.databinding.FragmentStoryDetailsBinding
import io.github.reactivecircus.streamlined.ui.ScreenForAnalytics
import io.github.reactivecircus.streamlined.ui.viewmodel.fragmentViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.collect

class StoryDetailsFragment @Inject constructor(
    private val viewModelFactory: StoryDetailsViewModel.Factory
) : Fragment(R.layout.fragment_story_details), ScreenForAnalytics {

    private val viewModel: StoryDetailsViewModel by fragmentViewModel {
        val storyId = requireNavInput<StoryDetailsInput>().storyId
        viewModelFactory.create(storyId)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        enterTransition = MaterialElevationScale(true)
        returnTransition = MaterialElevationScale(false)

        val binding = FragmentStoryDetailsBinding.bind(view)

        binding.toolbar.title = "Story title"

        // TODO transparent navigationBarColor for API 29+; #B3FFFFFF (light) and #B3000000 (night) for API < 29

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.rendering.collect {
                // TODO
            }
        }
    }
}
