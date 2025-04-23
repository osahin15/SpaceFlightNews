package com.onursahin.domain.usecase

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.onursahin.domain.model.News

object NewsDiffCallback : DiffUtil.ItemCallback<News>() {
    override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
        return oldItem == newItem
    }
}

class NoopListCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {  }
    override fun onRemoved(position: Int, count: Int) { }
    override fun onMoved(fromPosition: Int, toPosition: Int) {  }
    override fun onChanged(position: Int, count: Int, payload: Any?) {  }
}