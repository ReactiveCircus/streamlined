package io.github.reactivecircus.streamlined.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.home.databinding.ItemEmptyPlaceholderBinding
import io.github.reactivecircus.streamlined.home.databinding.ItemMainStoryBinding
import io.github.reactivecircus.streamlined.home.databinding.ItemReadMoreHeadlinesBinding
import io.github.reactivecircus.streamlined.home.databinding.ItemSectionHeaderBinding
import io.github.reactivecircus.streamlined.home.databinding.ItemStoryBinding
import reactivecircus.blueprint.ui.extension.isAnimationOn
import io.github.reactivecircus.streamlined.design.R as ThemeResource

internal class FeedsListAdapter(
    private val actionListener: ActionListener
) : ListAdapter<FeedItem, FeedViewHolder>(diffCallback) {

    private var lastAnimatedPosition = -1

    interface ActionListener {
        fun storyClicked(story: Story)
        fun bookmarkToggled(story: Story)
        fun moreButtonClicked(story: Story)
        fun readMoreHeadlinesButtonClicked()
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is FeedItem.Content -> {
                if (position > 0 && getItem(position - 1) is FeedItem.Header) {
                    R.layout.item_main_story
                } else {
                    R.layout.item_story
                }
            }
            is FeedItem.Header -> R.layout.item_section_header
            is FeedItem.TopHeadlinesFooter -> R.layout.item_read_more_headlines
            is FeedItem.Empty -> R.layout.item_empty_placeholder
            else -> throw IllegalArgumentException("Unknown view type at position: $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        return when (viewType) {
            R.layout.item_main_story -> {
                val binding = ItemMainStoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                MainStoryViewHolder(binding)
            }
            R.layout.item_story -> {
                val binding = ItemStoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                StoryViewHolder(binding)
            }
            R.layout.item_section_header -> {
                val binding = ItemSectionHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                SectionHeaderViewHolder(binding)
            }
            R.layout.item_read_more_headlines -> {
                val binding = ItemReadMoreHeadlinesBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ReadMoreHeadlinesViewHolder(binding)
            }
            R.layout.item_empty_placeholder -> {
                val binding = ItemEmptyPlaceholderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                EmptyPlaceholderViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        when (holder) {
            is MainStoryViewHolder -> {
                holder.bind(
                    story = (getItem(position) as FeedItem.Content).story,
                    isLastItem = position == itemCount - 1,
                    actionListener = actionListener
                )
            }
            is StoryViewHolder -> {
                holder.bind(
                    story = (getItem(position) as FeedItem.Content).story,
                    isLastItem = position == itemCount - 1,
                    actionListener = actionListener
                )
            }
            is SectionHeaderViewHolder -> {
                holder.bind((getItem(position) as FeedItem.Header).feedType)
            }
            is ReadMoreHeadlinesViewHolder -> {
                holder.bind(actionListener)
            }
            is EmptyPlaceholderViewHolder -> {
                holder.bind((getItem(position) as FeedItem.Empty).feedType)
            }
        }

        if (position > lastAnimatedPosition && holder.itemView.context.isAnimationOn()) {
            val animation = AnimationUtils.loadAnimation(
                holder.itemView.context,
                ThemeResource.anim.slide_in_and_fade_in
            )
            holder.itemView.startAnimation(animation)
            lastAnimatedPosition = holder.bindingAdapterPosition
        }
    }

    override fun onViewDetachedFromWindow(holder: FeedViewHolder) {
        holder.itemView.clearAnimation()
    }
}

private val diffCallback: DiffUtil.ItemCallback<FeedItem> =
    object : DiffUtil.ItemCallback<FeedItem>() {
        override fun areItemsTheSame(
            oldItem: FeedItem,
            newItem: FeedItem
        ) = when (oldItem) {
            is FeedItem.Content -> {
                newItem is FeedItem.Content &&
                        oldItem.story.id == newItem.story.id &&
                        oldItem.feedType == newItem.feedType
            }
            is FeedItem.Header -> newItem is FeedItem.Header && oldItem.feedType == newItem.feedType
            is FeedItem.TopHeadlinesFooter -> newItem is FeedItem.TopHeadlinesFooter
            is FeedItem.Empty -> newItem is FeedItem.Empty && oldItem.feedType == newItem.feedType
        }

        override fun areContentsTheSame(
            oldItem: FeedItem,
            newItem: FeedItem
        ) = oldItem == newItem
    }
