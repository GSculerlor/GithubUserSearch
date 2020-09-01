package moe.ganen.github.utils

import android.graphics.drawable.Drawable
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.databinding.BindingAdapter
import androidx.paging.LoadState
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener

object BindingAdapter {

    @BindingAdapter(value = ["imageUrl", "imageRequestListener", "circleCrop"], requireAll = false)
    @JvmStatic
    fun bindCircleImage(
        imageView: ImageView,
        url: String?,
        listener: RequestListener<Drawable?>?,
        circleCrop: Boolean?
    ) {
        Glide.with(imageView.context).apply {
            val builder = load(url)
            if (circleCrop == true)
                builder.circleCrop()

            builder
                .listener(listener)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imageView)
        }
    }

    @BindingAdapter(value = ["showProgress"])
    @JvmStatic
    fun bindProgressBarVisibility(
        view: ProgressBar,
        loadState: LoadState
    ) {
        if (loadState is LoadState.Loading)
            view.visibility = VISIBLE
        else
            view.visibility = GONE
    }

    @BindingAdapter(value = ["showRetryButton"])
    @JvmStatic
    fun bindRetryButtonVisibility(
        view: Button,
        loadState: LoadState
    ) {
        if (loadState is LoadState.Error)
            view.visibility = VISIBLE
        else
            view.visibility = GONE
    }
}