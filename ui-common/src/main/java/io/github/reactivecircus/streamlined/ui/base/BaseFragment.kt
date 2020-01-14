package io.github.reactivecircus.streamlined.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import io.github.reactivecircus.analytics.AnalyticsApi
import javax.inject.Inject

abstract class BaseFragment<Binding : ViewBinding> : Fragment() {

    @Inject
    lateinit var analyticsApi: AnalyticsApi

    private var _binding: Binding? = null
    protected val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = provideViewBinding(inflater, container)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    abstract fun provideViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): Binding

    override fun onResume() {
        super.onResume()
//        activity?.run {
//            analyticsApi.setCurrentScreenName(this, javaClass.simpleName, javaClass.simpleName)
//        }
    }
}
