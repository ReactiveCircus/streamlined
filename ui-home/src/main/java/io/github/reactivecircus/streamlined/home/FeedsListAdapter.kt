package io.github.reactivecircus.streamlined.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import io.github.reactivecircus.streamlined.design.enableDefaultCornerRadius
import io.github.reactivecircus.streamlined.domain.model.Story
import io.github.reactivecircus.streamlined.home.databinding.ItemEmptyPlaceholderBinding
import io.github.reactivecircus.streamlined.home.databinding.ItemMainStoryBinding
import io.github.reactivecircus.streamlined.home.databinding.ItemReadMoreHeadlinesBinding
import io.github.reactivecircus.streamlined.home.databinding.ItemSectionHeaderBinding
import io.github.reactivecircus.streamlined.home.databinding.ItemStoryBinding
import io.github.reactivecircus.streamlined.ui.configs.AnimationConfigs
import io.github.reactivecircus.streamlined.ui.util.toFormattedDateString
import reactivecircus.blueprint.ui.extension.isAnimationOn
import reactivecircus.blueprint.ui.extension.setPrecomputedTextFuture
import io.github.reactivecircus.streamlined.design.R as ThemeResource

internal const val PUBLISHED_TIME_DATE_PATTERN = "MMM dd"

internal class FeedsListAdapter(
    private val actionListener: ActionListener,
    private val animationConfigs: AnimationConfigs?
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

        if (animationConfigs != null &&
            position > lastAnimatedPosition && holder.itemView.context.isAnimationOn()
        ) {
            val animation = AnimationUtils.loadAnimation(
                holder.itemView.context,
                ThemeResource.anim.slide_in_and_fade_in
            )
            animation.startOffset = (animationConfigs.defaultListItemAnimationStartOffset *
                    holder.adapterPosition).toLong()
            holder.itemView.startAnimation(animation)
            lastAnimatedPosition = holder.adapterPosition
        }
    }
}

internal sealed class FeedViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

private class MainStoryViewHolder(
    private val binding: ItemMainStoryBinding
) : FeedViewHolder(binding.root) {
    fun bind(
        story: Story,
        isLastItem: Boolean,
        actionListener: FeedsListAdapter.ActionListener
    ) {
        binding.storyImageView.run {
            enableDefaultCornerRadius()
            if (story.imageUrl != null) {
                isVisible = true
                load(story.imageUrl)
            } else {
                isVisible = false
                load(story.imageUrl)
            }
        }
        binding.storySourceTextView.setPrecomputedTextFuture(story.source)
        binding.storyTitleTextView.setPrecomputedTextFuture(story.title)
        binding.publishedTimeTextView.setPrecomputedTextFuture(
            // TODO convert to pretty time e.g. Xx minute(s) / hour(s) / day(s) ago
            story.publishedTime.toFormattedDateString(PUBLISHED_TIME_DATE_PATTERN)
        )
        binding.bookmarkButton.run {
            setIconResource(ThemeResource.drawable.ic_twotone_bookmark_border_24)
            setOnClickListener {
                actionListener.bookmarkToggled(story)
            }
        }
        binding.moreButton.setOnClickListener {
            actionListener.moreButtonClicked(story)
        }
        binding.divider.isVisible = !isLastItem
        itemView.setOnClickListener { actionListener.storyClicked(story) }
    }
}

private class StoryViewHolder(
    private val binding: ItemStoryBinding
) : FeedViewHolder(binding.root) {
    fun bind(
        story: Story,
        isLastItem: Boolean,
        actionListener: FeedsListAdapter.ActionListener
    ) {
        binding.storyImageView.run {
            enableDefaultCornerRadius()
            if (story.imageUrl != null) {
                isVisible = true
                load(story.imageUrl)
            } else {
                isVisible = false
                load(story.imageUrl)
            }
        }
        binding.storySourceTextView.setPrecomputedTextFuture(story.source)
        binding.storyTitleTextView.setPrecomputedTextFuture(story.title)
        binding.publishedTimeTextView.setPrecomputedTextFuture(
            // TODO convert to pretty time e.g. Xx minute(s) / hour(s) / day(s) ago
            story.publishedTime.toFormattedDateString(PUBLISHED_TIME_DATE_PATTERN)
        )
        binding.bookmarkButton.setIconResource(ThemeResource.drawable.ic_twotone_bookmark_border_24)
        binding.bookmarkButton.setOnClickListener {
            actionListener.bookmarkToggled(story)
        }
        binding.moreButton.setOnClickListener {
            actionListener.moreButtonClicked(story)
        }
        binding.divider.isVisible = !isLastItem
        itemView.setOnClickListener { actionListener.storyClicked(story) }
    }
}

private class SectionHeaderViewHolder(
    private val binding: ItemSectionHeaderBinding
) : FeedViewHolder(binding.root) {
    fun bind(feedType: FeedType) {
        binding.titleTextView.setPrecomputedTextFuture(
            if (feedType is FeedType.TopHeadlines) {
                itemView.context.getString(R.string.feed_type_top_headlines)
            } else {
                itemView.context.getString(R.string.feed_type_for_you)
            }
        )
    }
}

private class ReadMoreHeadlinesViewHolder(
    binding: ItemReadMoreHeadlinesBinding
) : FeedViewHolder(binding.root) {
    fun bind(actionListener: FeedsListAdapter.ActionListener) {
        itemView.setOnClickListener { actionListener.readMoreHeadlinesButtonClicked() }
    }
}

private class EmptyPlaceholderViewHolder(
    private val binding: ItemEmptyPlaceholderBinding
) : FeedViewHolder(binding.root) {
    fun bind(feedType: FeedType) {
        binding.noStoriesTextView.setPrecomputedTextFuture(
            if (feedType is FeedType.TopHeadlines) {
                itemView.context.getString(R.string.no_headline_stories_found)
            } else {
                itemView.context.getString(R.string.no_personalized_stories_found)
            }
        )
        binding.divider.isVisible = feedType is FeedType.TopHeadlines
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

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: FeedItem,
            newItem: FeedItem
        ) = oldItem == newItem
    }
