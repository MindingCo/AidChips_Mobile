package com.minding.aidchips

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.minding.aidchips.ui.home.ClientViewModel

class ProfileFragment : Fragment(), OnClickListener
{
    companion object { fun newInstance() = ProfileFragment() }

    private lateinit var viewModel: ClientViewModel

    override fun onCreateView ( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ) : View =
        inflater.inflate(R.layout.fragment_profile, container, false)

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClientViewModel::class.java)

        view!!.findViewById<Button>(R.id.button_go_settings).setOnClickListener(this)

        view!!.findViewById<TextView>(R.id.fprofile_name).text = SavedData().getStringSavedData(view!!.context, SavedData.NameGroup.SESSION, SavedData.Elements.Session.NAME)
        view!!.findViewById<TextView>(R.id.mail).text = SavedData().getStringSavedData(view!!.context, SavedData.NameGroup.SESSION, SavedData.Elements.Session.EMAIL)
        view!!.findViewById<TextView>(R.id.tel).text = SavedData().getStringSavedData(view!!.context, SavedData.NameGroup.SESSION, SavedData.Elements.Session.TEL)
    }
    override fun onClick(v: View) =
        when (v.id)
        {
            R.id.button_go_settings -> startActivity(Intent(context, SettingsActivity::class.java))
            else -> {}
        }
}