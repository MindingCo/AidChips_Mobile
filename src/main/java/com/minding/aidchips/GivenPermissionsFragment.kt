package com.minding.aidchips

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.minding.aidchips.ui.home.ClientViewModel

class GivenPermissionsFragment : Fragment()
{
    companion object { fun newInstance() = GivenPermissionsFragment() }

    private lateinit var viewModel: ClientViewModel

    override fun onCreateView ( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ) : View =
        inflater.inflate(R.layout.fragment_given_permissions, container, false)
    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClientViewModel::class.java)

        val permissions: ArrayList<Permission> = ArrayList()

        permissions.add(Permission(123,1 ,"Gustavo Peduzzi", "5583599322", R.drawable.ic_launcher_background))
        permissions.add(Permission(123,2 ,"Sebastian Ruiz", "5583599322", R.drawable.ic_launcher_background))
        permissions.add(Permission(123,3 ,"Edgar Varillas", "5583599322", R.drawable.ic_launcher_background))
//        permissions.Add(Permission(124123,4 ,"Juanki el loco", "5583599322", R.drawable.ic_launcher_background))
//        permissions.Add(Permission(124123,5 ,"Gabo Lazaro alv", "5583599322", R.drawable.ic_launcher_background))
//        permissions.Add(Permission(124123,6 ,"Gustavo Peduzzi", "5583599322", R.drawable.ic_launcher_background))
//        permissions.Add(Permission(124123,7 ,"Sebastian Ruiz", "5583599322", R.drawable.ic_launcher_background))
//        permissions.Add(Permission(124123,8 ,"Edgar Varillas", "5583599322", R.drawable.ic_launcher_background))
//        permissions.Add(Permission(124123,9 ,"Juanki el loco", "5583599322", R.drawable.ic_launcher_background))

        view!!.findViewById<ListView>(R.id.list_given_permissions).adapter = GivenPermissionsAdapter(permissions)
    }
}