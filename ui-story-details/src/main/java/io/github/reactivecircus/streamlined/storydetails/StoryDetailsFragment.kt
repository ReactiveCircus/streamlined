package io.github.reactivecircus.streamlined.storydetails

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import io.github.reactivecircus.streamlined.storydetails.databinding.FragmentStoryDetailsBinding
import io.github.reactivecircus.streamlined.ui.Screen
import io.github.reactivecircus.streamlined.ui.viewmodel.fragmentViewModel
import javax.inject.Inject
import javax.inject.Provider

class StoryDetailsFragment @Inject constructor(
    private val viewModelProvider: Provider<StoryDetailsViewModel.Factory>
) : Fragment(R.layout.fragment_story_details), Screen {

    private val viewModel: StoryDetailsViewModel by fragmentViewModel {
        val id = requireArguments().getLong(ARG_STORY_ID)
        viewModelProvider.get().create(id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentStoryDetailsBinding.bind(view)

        binding.toolbar.title = "Story title..."
    }

    companion object {
        const val ARG_STORY_ID = "ARG_STORY_ID"
    }
}
