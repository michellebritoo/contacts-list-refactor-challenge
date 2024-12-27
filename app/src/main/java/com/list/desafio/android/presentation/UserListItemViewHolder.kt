package com.list.desafio.android.presentation

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.list.desafio.android.R
import com.list.desafio.android.data.model.UserResponse
import com.list.desafio.android.databinding.ListItemUserBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class UserListItemViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val userItem = ListItemUserBinding.bind(itemView)

    fun bind(user: UserResponse) {
        userItem.name.text = user.name
        userItem.username.text = user.username
        userItem.progressBar.visibility = View.VISIBLE
        Picasso.get().load(user.img).error(R.drawable.ic_round_account_circle)
            .into(
                userItem.picture, object : Callback {
                    override fun onSuccess() {
                        userItem.progressBar.visibility = View.GONE
                    }

                    override fun onError(e: Exception?) {
                        userItem.progressBar.visibility = View.GONE
                    }
                }
            )
    }
}
