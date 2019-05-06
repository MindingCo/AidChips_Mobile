package com.minding.aidchips

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient

class SettingsAdapter(private  var settings: ArrayList<Settings>) : BaseAdapter()
{
    @SuppressLint("ViewHolder")
    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View =
        LayoutInflater.from(parent!!.context).inflate(R.layout.item, parent, false).apply {
            findViewById<TextView>(R.id.item_title).text = settings[pos].title
            findViewById<TextView>(R.id.item_desc).text = settings[pos].desc
            findViewById<ImageView>(R.id.item_icon).setImageResource(settings[pos].icon)

            setOnClickListener {
                when (settings[pos].id)
                {
                    1 -> //     Update data
                    {

                    }
                    2 -> //     Notice of Privacy
                    {

                    }
                    3 -> //     About
                    {

                    }
                    4 -> //     Log out
                    {
                        (context as Activity).apply {
                            SavedData().setSavedData(this, SavedData.NameGroup.SESSION, SavedData.Elements.Session.LOGED, false)
                            finish()
                            startActivity(Intent(this, LoginActivity::class.java))
                            overridePendingTransition(R.anim.fade_in, R.anim.nothing)
                        }
                    }
                }
            }
        }
    override fun getItemId(pos: Int): Long =
        settings[pos].id.toLong()
    override fun getCount(): Int =
        settings.size
    override fun getItem(pos: Int): Any =
        settings[pos]
    class Settings (var id:Int, var title: String, var desc: String, var icon: Int)
}