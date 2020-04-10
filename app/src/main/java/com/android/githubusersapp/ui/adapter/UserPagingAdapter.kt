package com.android.githubusersapp.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.githubusersapp.R
import com.android.githubusersapp.depth.rx.RxState
import com.android.githubusersapp.domain.model.User
import kotlinx.android.synthetic.main.item_error.view.*
import kotlinx.android.synthetic.main.item_user.view.*
import com.android.githubusersapp.utils.setImageView


class UserPagingAdapter() : PagedListAdapter<User, RecyclerView.ViewHolder>(diffCallback) {

    private var paginationState: RxState<List<User>>? = null

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            R.layout.item_user -> UserPagingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_user,
                    parent,
                    false
                )
            )
            R.layout.item_error -> ErrorViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_error,
                    parent,
                    false
                )
            )
            R.layout.item_loading -> LoadingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_loading,
                    parent,
                    false
                )
            )
            R.layout.item_empty -> EmptyViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_empty,
                    parent,
                    false
                )
            )
            else -> UserPagingViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.item_user,
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.item_loading -> (holder as LoadingViewHolder)
            R.layout.item_user -> (holder as UserPagingViewHolder).bind(getItem(position))
            R.layout.item_error -> (holder as ErrorViewHolder).bind(paginationState as RxState.Error<List<User>>)
            R.layout.item_empty -> (holder as EmptyViewHolder)
            else -> (holder as LoadingViewHolder)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if (isAddData()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (isAddData() && position == itemCount - 1) {
            when (paginationState) {
                is RxState.Loading -> R.layout.item_loading
                is RxState.Error -> R.layout.item_error
                is RxState.Empty<List<User>> -> R.layout.item_empty
                else -> R.layout.item_error
            }
        } else {
            R.layout.item_user
        }
    }

    private fun isAddData(): Boolean {
        return paginationState != null && paginationState != RxState.success(currentList)
    }

    inner class UserPagingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(user: User?) {
            with(itemView) {
                user?.apply {
                    imgUser.setImageView(context, user.avatar, pbUser, R.drawable.ic_sentiment_very_dissatisfied_primary_24dp)
                    tvName.text = user.name
                }
            }
        }
    }

    inner class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)
    inner class ErrorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(error: RxState.Error<List<User>>) {
            with(itemView) {
                tvError.text = error.message
            }
        }
    }
    inner class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    fun setPaginationState(newState: RxState<List<User>>?) {
        Log.d("paging", "old state $paginationState")
        Log.d("paging", "new state $newState")
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = this.paginationState
                val isDataAdded = isAddData()
                this.paginationState = newState
                val isAddData = isAddData()
                if (isDataAdded != isAddData) {
                    if (isDataAdded) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (isAddData && previousState !== newState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }


}