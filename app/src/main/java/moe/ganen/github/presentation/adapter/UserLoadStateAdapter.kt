package moe.ganen.github.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import moe.ganen.github.R
import moe.ganen.github.databinding.ItemLoadStateBinding
import moe.ganen.github.utils.adapter.DataBoundViewHolder

class UserLoadStateAdapter(private val adapter: UserPagingAdapter) :
    LoadStateAdapter<DataBoundViewHolder<ItemLoadStateBinding>>() {

    override fun onBindViewHolder(
        holder: DataBoundViewHolder<ItemLoadStateBinding>,
        loadState: LoadState
    ) {
        holder.binding.loadState = loadState
        holder.binding.retryButton.setOnClickListener { adapter.retry() }

        holder.binding.executePendingBindings()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): DataBoundViewHolder<ItemLoadStateBinding> {
        val binding = DataBindingUtil.inflate<ItemLoadStateBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_load_state,
            parent,
            false
        )

        return DataBoundViewHolder(binding)
    }

}