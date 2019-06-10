package com.minding.aidchips

import android.annotation.SuppressLint
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

class AlertsAdapter(private  var notifs: ArrayList<Notif>) : BaseAdapter()
{
    class Notif(var usuario: User, var typeOf: Int?, var date: String, var idChip: String)
//    TypeOf if is false, normal notif, else if true, request notif

    @SuppressLint("ViewHolder")
    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View =
        LayoutInflater.from(parent!!.context).inflate(R.layout.item, parent, false).apply {

            findViewById<TextView>(R.id.item_desc).text = notifs[pos].date
            findViewById<ImageView>(R.id.fprofile_image).setImageBitmap(notifs[pos].usuario.image)

            if (notifs[pos].typeOf != null)
            {
                findViewById<RelativeLayout>(R.id.item_after_tittle).addView(inflate(parent.context, R.layout.after_title_alert, null).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                })

                findViewById<TextView>(R.id.fprofile_name).text = if (notifs[pos].typeOf == 0)
                                                                      parent.context.getText(R.string.notify_invited)
                                                                  else
                                                                      parent.context.getText(R.string.notify_emergency)

                val itemAfterTitle= findViewById<TextView>(R.id.item_after_tittle).buildCollapsible()

                setOnClickListener { itemAfterTitle.toggle() }
            }
            else
            {
                findViewById<RelativeLayout>(R.id.item_after_tittle).addView(inflate(parent.context, R.layout.after_title_request_alert, null).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                })
                findViewById<ConstraintLayout>(R.id.item_extra).addView(inflate(parent.context, R.layout.extra_request_alert, null).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                })

                findViewById<TextView>(R.id.fprofile_name).text = parent.context.getText(R.string.notify_request)
                findViewById<TextView>(R.id.item_chip).text = notifs[pos].idChip.toString()

                val itemAfterTitle= findViewById<TextView>(R.id.item_after_tittle).buildCollapsible()
                val itemExtra = findViewById<TextView>(R.id.item_extra).buildCollapsible()

                setOnClickListener { itemExtra.toggle(); itemAfterTitle.toggle() }
            }
            findViewById<TextView>(R.id.item_user).text = notifs[pos].usuario.nombre
            findViewById<TextView>(R.id.item_tel).text = notifs[pos].usuario.tel


    }
    override fun getItemId(pos: Int): Long =
        notifs[pos].usuario.id!!.toLong()
    override fun getCount(): Int =
        notifs.size
    override fun getItem(pos: Int): Any =
        notifs[pos]
}