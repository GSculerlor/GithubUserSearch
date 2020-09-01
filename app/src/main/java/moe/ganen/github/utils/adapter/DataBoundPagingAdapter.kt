package moe.ganen.github.utils.adapter

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil

abstract class DataBoundPagingAdapter<T : Any, V : ViewDataBinding>(
    diffCallback: DiffUtil.ItemCallback<T>
) : PagingDataAdapter<T, DataBoundViewHolder<V>>(
    diffCallback = diffCallback
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder<V> {
        val binding = createBinding(parent)
        return DataBoundViewHolder(binding)
    }

    protected abstract fun createBinding(parent: ViewGroup): V

    override fun onBindViewHolder(holder: DataBoundViewHolder<V>, position: Int) {
        bind(holder.binding, getItem(position), position)
        holder.binding.executePendingBindings()
    }

    protected abstract fun bind(binding: V, item: T?, position: Int)
}
