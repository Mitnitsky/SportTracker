package com.technion.fitracker.adapters


import android.content.Context
import android.content.Intent
import com.technion.fitracker.R
import com.technion.fitracker.UserLandingPageActivity
import com.technion.fitracker.models.SearchFireStoreModel
import com.technion.fitracker.adapters.SearchFireStoreAdapter.ViewHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions


class SearchFireStoreAdapter(options: FirestoreRecyclerOptions<SearchFireStoreModel>, input_activity: Context) :
        FirestoreRecyclerAdapter<SearchFireStoreModel, ViewHolder>(options) {


    var mOnItemClickListener: View.OnClickListener? = null
    var activity = input_activity


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setTag(this)
            view.setOnClickListener(mOnItemClickListener)
        }

        var name: TextView = view.findViewById(R.id.search_card)
        var user_image: ImageView = view.findViewById(R.id.search_imageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_ele, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, item: SearchFireStoreModel) {
        holder.name.text = item.name
        if (!item?.photoURL.isNullOrEmpty()) {
                    Glide.with(activity) //1
                    .load(item?.photoURL)
                    .placeholder(R.drawable.user_avatar)
                    .error(R.drawable.user_avatar)
                    .skipMemoryCache(true) //2
                    .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                    .transform(CircleCrop()) //4
                    .into(holder.user_image)
        }
        holder.itemView.setOnClickListener {
            val user_landing_page = Intent(holder.itemView.context, UserLandingPageActivity::class.java)
            user_landing_page.putExtra("user_name", item.name)
            user_landing_page.putExtra("photo_url", item?.photoURL)
            user_landing_page.putExtra("uid", item?.uid)
            user_landing_page.putExtra("type", item?.type)
            holder.itemView.context.startActivity(user_landing_page)
        }
    }
}







