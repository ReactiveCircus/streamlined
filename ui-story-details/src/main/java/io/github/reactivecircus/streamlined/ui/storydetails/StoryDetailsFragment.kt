package io.github.reactivecircus.streamlined.ui.storydetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import io.github.reactivecircus.streamlined.navigator.input.StoryDetailsInput
import io.github.reactivecircus.streamlined.navigator.requireNavInput
import io.github.reactivecircus.streamlined.ui.common.ScreenForAnalytics
import io.github.reactivecircus.streamlined.ui.common.util.collectWhenStarted
import io.github.reactivecircus.streamlined.ui.common.viewmodel.fragmentViewModel
import io.github.reactivecircus.streamlined.ui.storydetails.databinding.FragmentStoryDetailsBinding
import javax.inject.Inject

@AndroidEntryPoint
class StoryDetailsFragment : Fragment(R.layout.fragment_story_details), ScreenForAnalytics {

    @Inject
    lateinit var viewModelFactory: StoryDetailsViewModel.Factory

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

        collectWhenStarted(viewModel.rendering) {
            // TODO
        }
    }
}
