package com.android.githubusersapp.ui.main

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubusersapp.R
import com.android.githubusersapp.depth.rx.RxState
import com.android.githubusersapp.domain.model.User
import com.android.githubusersapp.ui.adapter.UserAdapter
import com.android.githubusersapp.ui.viewmodel.UserViewModel
import com.kennyc.view.MultiStateView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_error.*
import org.jetbrains.anko.sdk27.coroutines.onEditorAction
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val userViewModel: UserViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtUser.onEditorAction { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = edtUser.text.toString()
                observeUser(query)
            }
        }
    }

    private fun observeUser(query: String) {
        userViewModel.getUsers(query).observe(this@MainActivity, Observer {
            showUser(it)
        })

        userViewModel.getInitialRxState().observe(this@MainActivity, Observer {
            when (it) {
                is RxState.Loading -> msvUser.viewState = MultiStateView.ViewState.LOADING
                is RxState.Empty -> msvUser.viewState = MultiStateView.ViewState.EMPTY
                is RxState.Success -> msvUser.viewState = MultiStateView.ViewState.CONTENT
                is RxState.Error -> {
                    msvUser.viewState = MultiStateView.ViewState.ERROR
                    btnRetry.setOnClickListener {
                        observeUser(query)
                    }
                }
            }
        })
    }

    private fun showUser(users: PagedList<User>) {
        val userAdapter = UserAdapter {
            userViewModel.doRetry()
        }

        userAdapter.submitList(users)
        rvUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }

        userViewModel.getPaginationRxState().observe(this, Observer {
            userAdapter.setPaginationState(it)
        })
    }
}
