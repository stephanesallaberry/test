package fr.stephanesallaberry.news.android.transport.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import fr.stephanesallaberry.news.android.R
import fr.stephanesallaberry.news.android.databinding.HomeFragmentBinding
import fr.stephanesallaberry.news.android.domain.external.entity.Article
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe

class HomeFragment : Fragment(R.layout.home_fragment) {

    private val viewModel by viewModel<HomeViewModel>()

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
        binding.swipeRefreshLayout.setOnRefreshListener {
            refreshData()
        }
    }

    private fun refreshData() {
        viewModel.getArticles()
        binding.swipeRefreshLayout.isRefreshing = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    private fun render(state: HomeScreenState) {
        binding.loader.isVisible = state.isLoading
        binding.swipeRefreshLayout.isVisible = !state.isLoading
        val list = view?.findViewById<RecyclerView>(R.id.homeList) ?: return
        if (list.adapter == null) {
            list.adapter = ArticlesAdapter(state.articles) { displayItem(it) }
        } else {
            (list.adapter as? ArticlesAdapter)?.updateData(state.articles)
        }
    }

    private fun displayItem(article: Article?) {
        article?.let {
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
