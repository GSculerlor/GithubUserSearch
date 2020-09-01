package moe.ganen.github.presentation.ui

import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import moe.ganen.github.R
import moe.ganen.github.databinding.FragmentMainBinding
import moe.ganen.github.presentation.adapter.UserLoadStateAdapter
import moe.ganen.github.presentation.adapter.UserPagingAdapter
import moe.ganen.github.presentation.viewmodel.SearchViewModel
import moe.ganen.github.utils.wrapper.autoCleared
import org.koin.android.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var binding by autoCleared<FragmentMainBinding>()
    private var adapter by autoCleared<UserPagingAdapter>()

    private val viewModel: SearchViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val dataBinding = DataBindingUtil.inflate<FragmentMainBinding>(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )

        binding = dataBinding

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        setupObserver()
    }

    private fun setupUi() {
        val adapter = UserPagingAdapter()
        this.adapter = adapter

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            rvUserList.adapter = adapter.withLoadStateHeaderAndFooter(
                header = UserLoadStateAdapter(adapter),
                footer = UserLoadStateAdapter(adapter)
            )
            swipeUserList.setOnRefreshListener {
                adapter.refresh()
            }
            inputQuery.setOnKeyListener { _, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    updateQuery()
                    true
                } else {
                    false
                }
            }
            inputQuery.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    updateQuery()
                    true
                } else {
                    false
                }
            }
        }
    }

    private fun setupObserver() {
        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow.collectLatest { loadStates ->
                binding.swipeUserList.isRefreshing = loadStates.refresh is LoadState.Loading

                binding.textNoResult.visibility =
                    if (loadStates.refresh is LoadState.NotLoading && adapter.itemCount == 0)
                        View.VISIBLE
                    else
                        View.GONE

                if (loadStates.refresh is LoadState.Error)
                    Toast.makeText(
                        requireContext(),
                        (loadStates.refresh as? LoadState.Error)?.error?.message,
                        Toast.LENGTH_SHORT
                    ).show()
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.searchResultFlow.collectLatest {
                adapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            adapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .filter { it.refresh is LoadState.NotLoading }
                .collect { binding.rvUserList.scrollToPosition(0) }
        }
    }

    private fun updateQuery() {
        binding.inputQuery.text.trim().toString().let {
            if (it.isNotBlank()) {
                viewModel.search(it)
            }
        }
    }
}