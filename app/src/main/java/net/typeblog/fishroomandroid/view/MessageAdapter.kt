package net.typeblog.fishroomandroid.view

import android.content.Context
import android.view.View
import android.view.LayoutInflater
import android.widget.TextView
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

import kotlin.collections.MutableList

import net.typeblog.fishroomandroid.R
import net.typeblog.fishroomandroid.model.Message

/**
 * Created by peter on 6/15/16.
 */
class MessageAdater(val mItemList: MutableList<Message>) : RecyclerView.Adapter<MessageAdater.ViewHolder>() {
    var mInflater: LayoutInflater? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        mInflater = mInflater ?: parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val itemView = mInflater?.inflate(R.layout.msg_item, parent, false)

        return ViewHolder(itemView as View)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val msg = mItemList[itemCount - position - 1]
        holder.content.text = msg.content
        holder.sender.text = msg.sender
        holder.from.text = msg.channel
        holder.time.text = "${msg.time}"
    }

    override fun getItemCount(): Int {
        return mItemList.size
    }

    class ViewHolder(
            val itemView: View,
            val content: TextView = itemView.findViewById(R.id.content) as TextView,
            val sender: TextView = itemView.findViewById(R.id.sender) as TextView,
            val from: TextView = itemView.findViewById(R.id.from) as TextView,
            val time: TextView = itemView.findViewById(R.id.time) as TextView
    ) : RecyclerView.ViewHolder(itemView) {

    }
}
