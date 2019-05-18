package com.minding.aidchips

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.minding.aidchips.HomeAdapter.HomeViewHolder

class HomeAdapter(private  var chips: ArrayList<Chip>) : RecyclerView.Adapter<HomeViewHolder>()
{
        class Chip (var nSerial: String, var name: String, var tel: String)

    class HomeViewHolder(view: View): RecyclerView.ViewHolder(view)
    {
        var deleter: ImageButton =  view.findViewById(R.id.cardview_btn_delete)
        var owner: TextView = view.findViewById(R.id.cardview_owner)
        var serial: TextView = view.findViewById(R.id.cardview_serial)
        var tel: TextView = view.findViewById(R.id.cardview_tel)
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
        holder.deleter.setOnClickListener {
            deleteChip(chips[pos].nSerial, pos, it.context)
        }
    }

    private fun deleteChip(serial: String, pos: Int, ctx: Context)
    {
        val params : MutableMap<String, String> = HashMap()
        params["id"] = SavedData().getIntSavedData(ctx, SavedData.NameGroup.SESSION, SavedData.Elements.Session.ID).toString()
        params["nserie"] = serial
        params["owner"] = "true"

        DataBase().requestOperation(ctx, DataBase.Action.Del.PERMIT, DataBase.Method.POST, params)
        { completed ->
            when (completed)
            {
                true ->
                {
                    chips.removeAt(pos)
                    notifyItemRemoved(pos)
                }
                else -> Toast.makeText(ctx, "Eliminacion de permiso fallida", Toast.LENGTH_SHORT).show()
            }
        }
    }
}