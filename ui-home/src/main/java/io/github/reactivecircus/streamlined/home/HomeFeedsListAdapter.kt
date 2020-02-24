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
import io.github.reactivecircus.streamlined.home.databinding.ItemMainStoryBinding
import io.github.reactivecircus.streamlined.home.databinding.ItemReadMoreHeadlinesBinding
import io.github.reactivecircus.streamlined.home.databinding.ItemSectionHeaderBinding
import io.github.reactivecircus.streamlined.home.databinding.ItemStoryBinding
import io.github.reactivecircus.streamlined.ui.configs.AnimationConfigs
import io.github.reactivecircus.streamlined.ui.util.AdapterItem
import io.github.reactivecircus.streamlined.ui.util.toFormattedDateString
import reactivecircus.blueprint.ui.extension.isAnimationOn
import reactivecircus.blueprint.ui.extension.setPrecomputedTextFuture
import io.github.reactivecircus.streamlined.design.R as ThemeR

internal const val PUBLISHED_TIME_DATE_PATTERN = "MMM dd"

internal class HomeFeedsListAdapter(
    private val actionListener: ActionListener,
    private val animationConfigs: AnimationConfigs?
) : ListAdapter<AdapterItem<Story, FeedType, Unit>, HomeFeedsViewHolder>(diffCallback) {

    private var lastAnimatedPosition = -1

    interface ActionListener {
        fun storyClicked(story: Story)
        fun bookmarkToggled(story: Story)
        fun moreButtonClicked(story: Story)
        fun readMoreHeadlinesButtonClicked()
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AdapterItem.Content -> {
                if (position > 0 && getItem(position - 1) is AdapterItem.Header<FeedType>) {
                    R.layout.item_main_story
                } else {
                    R.layout.item_story
                }
            }
            is AdapterItem.Header -> R.layout.item_section_header
            is AdapterItem.Footer -> R.layout.item_read_more_headlines
            else -> throw IllegalArgumentException("Unknown view type at position: $position")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeFeedsViewHolder {
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
            else -> throw IllegalArgumentException("Unknown view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: HomeFeedsViewHolder, position: Int) {
        when (holder) {
            is MainStoryViewHolder -> {
                holder.bind(
                    story = (getItem(position) as AdapterItem.Content<Story>).data,
                    isLastItem = position == itemCount - 1,
                    actionListener = actionListener
                )
            }
            is StoryViewHolder -> {
                holder.bind(
                    story = (getItem(position) as AdapterItem.Content<Story>).data,
                    isLastItem = position == itemCount - 1,
                    actionListener = actionListener
                )
            }
            is SectionHeaderViewHolder -> {
                holder.bind((getItem(position) as AdapterItem.Header<FeedType>).headerData)
            }
            is ReadMoreHeadlinesViewHolder -> {
                holder.bind(actionListener)
            }
        }

        if (animationConfigs != null &&
            position > lastAnimatedPosition && holder.itemView.context.isAnimationOn()
        ) {
            val animation = AnimationUtils.loadAnimation(
                holder.itemView.context,
                ThemeR.anim.slide_in_and_fade_in
            )
            animation.startOffset = (animationConfigs.defaultListItemAnimationStartOffset *
                    holder.adapterPosition).toLong()
            holder.itemView.startAnimation(animation)
            lastAnimatedPosition = holder.adapterPosition
        }
    }
}

internal sealed class HomeFeedsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

private class MainStoryViewHolder(
    private val binding: ItemMainStoryBinding
) : HomeFeedsViewHolder(binding.root) {
    fun bind(
        story: Story,
        isLastItem: Boolean,
        actionListener: HomeFeedsListAdapter.ActionListener
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
            setIconResource(ThemeR.drawable.ic_twotone_bookmark_border_24)
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
) : HomeFeedsViewHolder(binding.root) {
    fun bind(
        story: Story,
        isLastItem: Boolean,
        actionListener: HomeFeedsListAdapter.ActionListener
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
        binding.bookmarkButton.setIconResource(ThemeR.drawable.ic_twotone_bookmark_border_24)
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
) : HomeFeedsViewHolder(binding.root) {
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
) : HomeFeedsViewHolder(binding.root) {
    fun bind(actionListener: HomeFeedsListAdapter.ActionListener) {
        itemView.setOnClickListener { actionListener.readMoreHeadlinesButtonClicked() }
    }
}

private val diffCallback: DiffUtil.ItemCallback<AdapterItem<Story, FeedType, Unit>> =
    object : DiffUtil.ItemCallback<AdapterItem<Story, FeedType, Unit>>() {

        override fun areItemsTheSame(
            oldItem: AdapterItem<Story, FeedType, Unit>,
            newItem: AdapterItem<Story, FeedType, Unit>
        ) = when (oldItem) {
            is AdapterItem.Content -> {
                newItem is AdapterItem.Content && oldItem.data.id == oldItem.data.id
            }
            is AdapterItem.Header -> newItem is AdapterItem.Header
            is AdapterItem.Footer -> newItem is AdapterItem.Footer
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: AdapterItem<Story, FeedType, Unit>,
            newItem: AdapterItem<Story, FeedType, Unit>
        ) = oldItem == newItem
    }
