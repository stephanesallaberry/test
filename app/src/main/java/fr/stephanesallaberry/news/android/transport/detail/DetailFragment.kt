package fr.stephanesallaberry.news.android.transport.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import fr.stephanesallaberry.news.android.R
import fr.stephanesallaberry.news.android.databinding.DetailFragmentBinding
import fr.stephanesallaberry.news.android.domain.external.entity.Article
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber

class DetailFragment : Fragment(R.layout.detail_fragment) {
    private val args: DetailFragmentArgs by navArgs()
    private val viewModel by viewModel<DetailViewModel> { parametersOf(args.detailArg) }

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
        state.article?.let { dataUnwrapped ->
            setToolbar(dataUnwrapped)

            Glide.with(this)
                .load(dataUnwrapped.urlToImage)
                .into(binding.detailImage)

            binding.detailDescription.text = dataUnwrapped.description
        }
    }

    private fun setToolbar(article: Article) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = article.title
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
