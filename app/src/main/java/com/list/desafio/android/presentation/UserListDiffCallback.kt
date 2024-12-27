package com.list.desafio.android.presentation

import androidx.recyclerview.widget.DiffUtil
import com.list.desafio.android.data.model.UserResponse

class UserListDiffCallback(
    private val oldList: List<UserResponse>,
    private val newList: List<UserResponse>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].username.equals(newList[newItemPosition].username)
    }

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return true
    }
}