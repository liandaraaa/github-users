package com.android.githubusersapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.githubusersapp.R
import com.android.githubusersapp.depth.rx.RxState
import com.android.githubusersapp.domain.model.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_error.view.*
import kotlinx.android.synthetic.main.item_user.view.*

class UserPagingAdapter():PagedListAdapter<User,RecyclerView.ViewHolder>(diffCallback){

    private var paginationState:RxState<List<User>>? = null

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(paginationState){
            is RxState.Success -> UserPagingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))
            is RxState.Error -> ErrorViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_error, parent, false))
            is RxState.Loading -> LoadingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loading, parent, false))
            else -> UserPagingViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(paginationState){
            is RxState.Loading -> (holder as LoadingViewHolder)
            is RxState.Success -> (holder as UserPagingViewHolder).bind(getItem(position))
            is RxState.Error -> (holder as ErrorViewHolder).bind(paginationState as RxState.Error<List<User>>)
            else -> (holder as LoadingViewHolder)
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + if(isAddData()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return when(paginationState){
            is RxState.Loading -> R.layout.item_loading
            is RxState.Success -> R.layout.item_user
            is RxState.Error -> R.layout.item_error
            else -> R.layout.item_loading
        }
    }

    private fun isAddData():Boolean{
        return paginationState != null
    }

    inner class UserPagingViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bind(user:User?){
            with(itemView){
                user?.apply {
                    Glide.with(context).load(user.avatar).into(imgUser)
                    tvName.text = user.name
                }
            }
        }
    }

    inner class LoadingViewHolder(view: View):RecyclerView.ViewHolder(view){

    }

    inner class ErrorViewHolder(view: View):RecyclerView.ViewHolder(view){
        fun bind(error:RxState.Error<List<User>>){
            with(itemView){
                tvError.text = error.message
            }
        }
    }

    fun setNetworkState(newNetworkState: RxState<List<User>>?) {
        if (currentList != null) {
            if (currentList!!.size != 0) {
                val previousState = this.paginationState
                val isDataAdded = isAddData()
                this.paginationState = newNetworkState
                val isAddData = isAddData()
                if (isDataAdded != isAddData) {
                    if (isDataAdded) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (isAddData && previousState !== newNetworkState) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }


}