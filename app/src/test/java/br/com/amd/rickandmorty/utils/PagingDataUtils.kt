package br.com.amd.rickandmorty.utils

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback

// help unwrap a Paging<Character>
class NoDiffCallback<T : Any> : DiffUtil.ItemCallback<T>() {
    override fun areItemsTheSame(oldItem: T, newItem: T): Boolean = false
    override fun areContentsTheSame(oldItem: T, newItem: T): Boolean = false
}

class ListUpdateTestCallback : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) = Unit
    override fun onRemoved(position: Int, count: Int) = Unit
    override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
    override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
}

