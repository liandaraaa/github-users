package com.android.githubusersapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.githubusersapp.R
import com.android.githubusersapp.domain.model.User
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*

class UserAdapter(
    private val context:Context,
    private val datas:List<User>
):RecyclerView.Adapter<UserAdapter.UserViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false))
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    inner class UserViewHolder(view:View):RecyclerView.ViewHolder(view){
        fun bind(user:User){
            with(itemView){
                Glide.with(context).load(user.avatar).into(imgUser)
                tvName.text = user.name
            }
        }
    }
}