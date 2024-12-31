package com.list.desafio.android.presentation.adapter

import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.list.desafio.android.R
import com.list.desafio.android.databinding.ListItemUserBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class UserListItemViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val userItem = ListItemUserBinding.bind(itemView)

    fun bind(user: UserUIModel) = with(userItem) {
        name.text = user.name
        username.text = user.username
        progressBar.isVisible = true
        Picasso.get().load(user.image).error(R.drawable.ic_round_account_circle)
            .into(
                picture, object : Callback {
                    override fun onSuccess() {
                        userItem.progressBar.isVisible = false
                    }

                    override fun onError(e: Exception?) {
                        userItem.progressBar.isVisible = false
                    }
                }
            )
    }
}
