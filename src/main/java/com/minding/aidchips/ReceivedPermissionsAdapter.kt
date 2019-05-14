package com.minding.aidchips

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

class ReceivedPermissionsAdapter(private  var permits: ArrayList<Permit>) : BaseAdapter()
{
    @SuppressLint("ViewHolder")
    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View =
        LayoutInflater.from(parent!!.context).inflate(R.layout.item, parent, false).apply {

            findViewById<RelativeLayout>(R.id.item_after_tittle).addView(inflate(parent.context, R.layout.after_title_permission, null).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            })
            findViewById<TextView>(R.id.fprofile_name).text = permits[pos].name
            findViewById<TextView>(R.id.item_tel).text = permits[pos].tel
            if (permits[pos].image != null)
                findViewById<ImageView>(R.id.fprofile_image).setImageResource(permits[pos].image!!)
            findViewById<TextView>(R.id.item_chip).text = permits[pos].nSerie

            val itemAfterTitle= findViewById<TextView>(R.id.item_after_tittle).buildCollapsible()

            setOnClickListener { itemAfterTitle.toggle() }
    }
    override fun getItemId(pos: Int): Long =
        permits[pos].idUser.toLong()
    override fun getCount(): Int =
        permits.size
    override fun getItem(pos: Int): Any =
        permits[pos]
}
/*class GivenPermissionsAdapter( context: Activity, idPermission : ArrayList<String>, private  var permissions: ArrayList<Permit>) : ArrayAdapter<String>(context, R.layout.item, idPermission)
{
    @SuppressLint("ViewHolder")
    override fun getView(pos: Int, view: View?, parent: ViewGroup): View =
        LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false).apply {
            findViewById<TextView>(R.nSerie.item_title).text = permissions[pos].name
            findViewById<TextView>(R.nSerie.item_desc).text = permissions[pos].tel
            findViewById<RelativeLayout>(R.nSerie.item_extra).addView(inflate(parent.context, R.layout.extra_given_permission, null))
            findViewById<ImageView>(R.nSerie.item_icon).setImageResource(permissions[pos].image)
            val itemExtra = findViewById<TextView>(R.nSerie.item_extra).buildCollapsible()
            val itemAfterTitle= findViewById<TextView>(R.nSerie.item_after_tittle).buildCollapsible()
            setOnClickListener { itemExtra.toggle(); itemAfterTitle.toggle() }
    }
}*/
/*
class GivenPermissionsAdapter(private var permissions: ArrayList<Permit>) : RecyclerView.Adapter<GivenPermissionsViewHolder>() {
    class GivenPermissionsViewHolder(v: View) : RecyclerView.ViewHolder(v)
    {
        var image: ImageView = v.findViewById(R.nSerie.item_icon)
        var name: TextView = v.findViewById(R.nSerie.item_title)
        var tel: TextView = v.findViewById(R.nSerie.item_desc)
    }
    override fun getItemCount() = permissions.size
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int) =
        GivenPermissionsViewHolder(v = LayoutInflater.from(p0.context).inflate(R.layout.item, p0, false)).also {
            //            val collapsible = it.name.buildCollapsible()
//            it.itemView.setOnClickListener { collapsible.toggle() }
        }
    override fun onBindViewHolder(vHolder: GivenPermissionsViewHolder, pos: Int)
    {
        vHolder.image.setImageResource(permissions[pos].image)
        vHolder.name.text =  permissions[pos].name
        vHolder.tel.text = permissions[pos].tel

        vHolder.name.buildCollapsible().collapseView()
        vHolder.itemView.setOnClickListener {
            val collapsible = it.item_title.buildCollapsible()
            collapsible.toggle()
//            notifyItemChanged(pos)
        }
    }
}*/