package com.list.desafio.android

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.list.desafio.android.data.model.UserResponse
import com.list.desafio.android.presentation.UserListAdapter
import com.list.desafio.android.presentation.UsersUIEvent
import com.list.desafio.android.presentation.UsersViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var adapter: UserListAdapter

    private val viewModel: UsersViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        observeEvents()
    }

    override fun onResume() {
        super.onResume()

        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.user_list_progress_bar)

        adapter = UserListAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.onStart()
    }

    private fun observeEvents() {
        lifecycleScope.launchWhenStarted {
            viewModel.viewState.collect { event ->
                when (event) {
                    is UsersUIEvent.ShowLoading -> showLoading()
                    is UsersUIEvent.HideLoading -> hideLoading()
                    is UsersUIEvent.ShowError -> showError()
                    is UsersUIEvent.ShowUsersList -> showUserList(event.list)
                }
            }
        }
    }

    private fun showLoading() {
        progressBar.isVisible = true
    }

    private fun hideLoading() {
        progressBar.isVisible = false
    }

    private fun showUserList(list: List<UserResponse>) {
        adapter.users = list
    }

    private fun showError() {
        Toast.makeText(
            this,
            getString(R.string.error),
            Toast.LENGTH_SHORT
        ).show()
    }
}
