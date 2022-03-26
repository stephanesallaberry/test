package com.appstud.template.android.transport.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.appstud.template.android.R
import com.appstud.template.android.databinding.DetailFragmentBinding
import com.appstud.template.android.domain.external.entity.Breed
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber

class BreedDetailFragment : Fragment(R.layout.detail_fragment) {
    private val args: BreedDetailFragmentArgs by navArgs()
    private val viewModel by viewModel<DetailViewModel> { parametersOf(args.detailArgBreed) }

    private var _binding: DetailFragmentBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    private fun render(state: DetailScreenState) {
        Timber.d("render $state")
        state.breed?.let { breedNotNull ->
            setToolbar(breedNotNull)

            Glide.with(this)
                .load(breedNotNull.image.url)
                .into(binding.detailImage)

            binding.detailDescription.text = breedNotNull.description
        }
    }

    private fun setToolbar(breed: Breed) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = breed.name
    }

    private fun handleSideEffect(sideEffect: DetailScreenSideEffect) {
        when (sideEffect) {
            is DetailScreenSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
