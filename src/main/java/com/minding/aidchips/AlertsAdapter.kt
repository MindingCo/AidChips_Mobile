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

class AlertsAdapter(private  var alerts: ArrayList<Alert>) : BaseAdapter()
{
    class Alert(var usuario: User, var access: String?, var date: String, var idChip: Int?, var typeOf: Boolean)
//    TypeOf if is false, normal notif, else if true, request notif

    @SuppressLint("ViewHolder")
    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View =
        LayoutInflater.from(parent!!.context).inflate(R.layout.item, parent, false).apply {

            findViewById<TextView>(R.id.item_desc).text = alerts[pos].date
            findViewById<ImageView>(R.id.item_icon).setImageBitmap(alerts[pos].usuario.image)

            if (!alerts[pos].typeOf)
            {
                findViewById<RelativeLayout>(R.id.item_after_tittle).addView(inflate(parent.context, R.layout.after_title_alert, null).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                })

                findViewById<TextView>(R.id.item_title).text = alerts[pos].access

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

                findViewById<TextView>(R.id.item_title).text = parent.context.getText(R.string.notify_request)
                findViewById<TextView>(R.id.item_chip).text = alerts[pos].idChip.toString()

                val itemAfterTitle= findViewById<TextView>(R.id.item_after_tittle).buildCollapsible()
                val itemExtra = findViewById<TextView>(R.id.item_extra).buildCollapsible()

                setOnClickListener { itemExtra.toggle(); itemAfterTitle.toggle() }
            }
            findViewById<TextView>(R.id.item_user).text = alerts[pos].usuario.nombre
            findViewById<TextView>(R.id.item_tel).text = alerts[pos].usuario.tel


    }
    override fun getItemId(pos: Int): Long =
        alerts[pos].usuario.id!!.toLong()
    override fun getCount(): Int =
        alerts.size
    override fun getItem(pos: Int): Any =
        alerts[pos]
}