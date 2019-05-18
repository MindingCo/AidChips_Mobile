package com.minding.aidchips

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.ProgressBar
import com.minding.aidchips.AlertsAdapter.Alert
import com.minding.aidchips.ui.home.ClientViewModel

class AlertsFragment : Fragment()
{
    companion object { fun newInstance() = AlertsFragment() }

    private lateinit var viewModel: ClientViewModel

    override fun onCreateView ( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ) : View =
        inflater.inflate(R.layout.fragment_given_permissions, container, false)
    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClientViewModel::class.java)

        val permissions: ArrayList<Alert> = ArrayList()

        permissions.add(Alert(User(null, "Gustavo Peduzzi", null, "5583599322", R.drawable.ic_launcher_background, requireContext(), null),"Acceso como emergencia" ,"19 septiempre 2019", null, false))
        permissions.add(Alert(User(null, "Gustavo Peduzzi", null, "5583599322", R.drawable.ic_launcher_background, requireContext(), null),"Acceso como emergencia" ,"19 septiempre 2019", null, false))
        permissions.add(Alert(User(null, "Gustavo Peduzzi", null, "5583599322", R.drawable.ic_launcher_background, requireContext(), null),"Acceso como emergencia" ,"19 septiempre 2019", null, false))
        permissions.add(Alert(User(null, "Gustavo Peduzzi", null, "5583599322", R.drawable.ic_launcher_background, requireContext(), null),null ,"19 septiempre 2019", 123124, true))

        view!!.findViewById<ListView>(R.id.list_given_permissions).adapter = AlertsAdapter(permissions)
        view!!.findViewById<ProgressBar>(R.id.loader).visibility = View.GONE
    }
}