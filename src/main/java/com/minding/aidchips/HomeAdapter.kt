package com.minding.aidchips

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.minding.aidchips.HomeAdapter.*

class HomeAdapter(private  var chips: ArrayList<Chip>) : RecyclerView.Adapter<HomeViewHolder>()
{
    class Chip (var nSerial: String, var name: String, var tel: String)

    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        var owner = view.findViewById<TextView>(R.id.cardview_owner)
        var serial = view.findViewById<TextView>(R.id.cardview_serial)
        var tel = view.findViewById<TextView>(R.id.cardview_tel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder =
        HomeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.cardciew_chip, parent, false))

    override fun getItemCount(): Int =
        chips.size

    override fun onBindViewHolder(holder: HomeViewHolder, pos: Int)
    {
        holder.owner.text = chips[pos].name
        holder.serial.text = chips[pos].nSerial
        holder.tel.text = chips[pos].tel
    }

}