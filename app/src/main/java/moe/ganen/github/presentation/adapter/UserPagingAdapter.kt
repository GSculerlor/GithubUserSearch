package moe.ganen.github.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import moe.ganen.github.R
import moe.ganen.github.databinding.ItemUserBinding
import moe.ganen.github.domain.model.User
import moe.ganen.github.utils.adapter.DataBoundPagingAdapter

class UserPagingAdapter : DataBoundPagingAdapter<User, ItemUserBinding>(
    diffCallback = object : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }
    }
) {

    override fun createBinding(parent: ViewGroup): ItemUserBinding {
        return DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_user,
            parent,
            false
        )
    }

    override fun bind(binding: ItemUserBinding, item: User?, position: Int) {
        binding.user = item
    }
}