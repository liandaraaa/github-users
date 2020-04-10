package com.android.githubusersapp.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.githubusersapp.R
import com.android.githubusersapp.depth.rx.RxState
import com.android.githubusersapp.domain.model.User
import com.android.githubusersapp.ui.adapter.UserAdapter
import com.android.githubusersapp.ui.adapter.UserPagingAdapter
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

        edtUser.onEditorAction { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = edtUser.text.toString()
                userViewModel.getPagingUsers(query).observe(this@MainActivity, Observer {
                    showPagingUser(it)
                })

                userViewModel.getInitialRxState().observe(this@MainActivity, Observer {
                    when (it) {
                        is RxState.Loading -> msvUser.viewState = MultiStateView.ViewState.LOADING
                        is RxState.Empty -> {
                            msvUser.viewState = MultiStateView.ViewState.EMPTY
                            with(msvUser.viewState) {
                                img_error.setImageResource(R.drawable.ic_sentiment_dissatisfied_accent_24dp)
                                tv_message.text = "Data tidak ditemukan"
                            }
                        }
                        is RxState.Success -> {
                            msvUser.viewState = MultiStateView.ViewState.CONTENT
                        }
                        is RxState.Error -> {
                            msvUser.viewState = MultiStateView.ViewState.ERROR
                            with(msvUser.viewState) {
                                img_error.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_primary_24dp)
                                tv_message.text = it.message
                            }
                        }
                    }
                })
            }
        }
    }

    private fun observeUser(query: String) {
        userViewModel.getUsers(query).observe(this, Observer {
            when (it) {
                is RxState.Loading -> msvUser.viewState = MultiStateView.ViewState.LOADING
                is RxState.Empty -> {
                    msvUser.viewState = MultiStateView.ViewState.EMPTY
                    with(msvUser.viewState) {
                        img_error.setImageResource(R.drawable.ic_sentiment_dissatisfied_accent_24dp)
                        tv_message.text = "Data tidak ditemukan"
                    }
                }
                is RxState.Success -> {
                    msvUser.viewState = MultiStateView.ViewState.CONTENT
                    showUser(it.data)
                }
                is RxState.Error -> {
                    msvUser.viewState = MultiStateView.ViewState.ERROR
                    with(msvUser.viewState) {
                        img_error.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_primary_24dp)
                        tv_message.text = it.message
                    }
                }
            }
        })

    }

    private fun showUser(users: List<User>) {
        val userAdapter = UserAdapter(this, users)
        rvUser.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }
    }

    private fun showPagingUser(users: PagedList<User>) {
        val userAdapter = UserPagingAdapter()
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
