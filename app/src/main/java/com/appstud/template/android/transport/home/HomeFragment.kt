package com.appstud.template.android.transport.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.appstud.template.android.R
import com.appstud.template.android.databinding.HomeFragmentBinding
import com.appstud.template.android.domain.external.entity.Breed
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber

class HomeFragment : Fragment(R.layout.home_fragment) {

    private val viewModel by viewModel<HomeViewModel>()
    private val sizePicturesInPixels by lazy {
        resources.getDimensionPixelSize(R.dimen.home_thumbnail_size)
    }

    private var _binding: HomeFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.breedsSwipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        viewModel.getBreeds()
        binding.breedsSwipeRefreshLayout.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    private fun render(state: HomeScreenState) {
        binding.loader.isVisible = state.isLoading
        binding.breedsSwipeRefreshLayout.isVisible = !state.isLoading
        val list = view?.findViewById<RecyclerView>(R.id.breedsList) ?: return
        if (list.adapter == null) {
            list.adapter = BreedsAdapter(sizePicturesInPixels, state.breeds) { displayBreed(it) }
        } else {
            (list.adapter as? BreedsAdapter)?.updateData(state.breeds)
        }
    }

    private fun displayBreed(breed: Breed?) {
        breed?.let {
            Timber.d("yoooooo $it")
            findNavController().navigate(
                HomeFragmentDirections.displayDetail(it)
            )
        }
    }

    private fun handleSideEffect(sideEffect: HomeScreenSideEffect) {
        when (sideEffect) {
            is HomeScreenSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
