package io.github.reactivecircus.streamlined.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.github.reactivecircus.analytics.AnalyticsApi
import io.github.reactivecircus.streamlined.home.databinding.FragmentHomeBinding
import io.github.reactivecircus.streamlined.navigator.Navigator
import io.github.reactivecircus.streamlined.ui.base.BaseFragment
import io.github.reactivecircus.streamlined.ui.viewmodel.fragmentViewModel
import javax.inject.Inject
import javax.inject.Provider

class HomeFragment @Inject constructor(
    analyticsApi: AnalyticsApi,
    private val navigator: Navigator,
    private val viewModelProvider: Provider<HomeViewModel>
) : BaseFragment(analyticsApi) {

    private val binding get() = view?.tag as FragmentHomeBinding

    private val viewModel: HomeViewModel by fragmentViewModel { viewModelProvider.get() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        view.tag = binding
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.title = getString(R.string.title_home)
    }
}
