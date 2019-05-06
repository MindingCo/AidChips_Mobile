package com.minding.aidchips

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.minding.aidchips.SettingsAdapter.*

class SettingsActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setTitle(R.string.title_settings)
        setToolbar()

        findViewById<ListView>(R.id.list_settings).adapter = SettingsAdapter(ArrayList<Settings>()
            .apply {
                add(Settings(1, getString(R.string.update_user), getString(R.string.update_user_desc), R.drawable.ic_avatar))
                add(Settings(2, getString(R.string.notice_privacy), getString(R.string.notice_privacy_desc), R.drawable.ic_avatar))
                add(Settings(3, getString(R.string.about), getString(R.string.about_desc), R.drawable.ic_avatar))
                add(Settings(4, getString(R.string.logout), getString(R.string.logout_desc), R.drawable.ic_avatar))
        })
    }
    @SuppressLint("PrivateResource")
    private fun setToolbar()
    {
        val toolbar: android.support.v7.widget.Toolbar  = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.apply {
            setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material)
            setDisplayHomeAsUpEnabled(true)
        }

        toolbar.setNavigationOnClickListener { onBackPressed() }
    }
}