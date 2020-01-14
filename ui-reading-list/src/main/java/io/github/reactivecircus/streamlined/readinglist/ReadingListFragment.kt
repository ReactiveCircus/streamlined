package io.github.reactivecircus.streamlined.readinglist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.reactivecircus.streamlined.readinglist.databinding.FragmentReadingListBinding
import io.github.reactivecircus.streamlined.ui.base.BaseFragment

class ReadingListFragment : BaseFragment<FragmentReadingListBinding>() {

    override fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReadingListBinding {
        return FragmentReadingListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = getString(R.string.title_reading_list)
    }
}
