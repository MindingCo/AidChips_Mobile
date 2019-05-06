
package com.minding.aidchips

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.View.inflate
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import com.minding.aidchips.ui.home.ClientViewModel


class HomeFragment : Fragment(), OnClickListener
{
    companion object { fun newInstance() = HomeFragment() }

    private lateinit var viewModel: ClientViewModel
    private lateinit var dialog: Dialog

    override fun onCreateView
    (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    )
    : View { return inflater.inflate(R.layout.fragment_home, container, false) }

    override fun onActivityCreated(savedInstanceState: Bundle?)
    {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ClientViewModel::class.java)

        view!!.findViewById<Button>(R.id.btnRChipLikeProperty).setOnClickListener(this)
        view!!.findViewById<Button>(R.id.btnRChip).setOnClickListener(this)
    }

    @SuppressLint("InflateParams", "ResourceAsColor")
    private fun showDialog()
    {
        val dialogView: View = inflate(context, R.layout.dialog_rchip_nopermission, null)

        dialogView.findViewById<ImageButton>(R.id.closeDialog).setOnClickListener(this)
        dialogView.findViewById<Button>(R.id.btn_request_permission).setOnClickListener(this)
        dialogView.findViewById<Button>(R.id.btn_emergency).setOnClickListener(this)

        val ctw = ContextThemeWrapper(context, R.style.Main)
        val builder = AlertDialog.Builder(ctw)
        builder.setView(dialogView)
        dialog = builder.create()

        dialog.show()
    }
    @SuppressLint("WrongConstant")
    override fun onClick(v: View) =
        when (v.id)
        {
            R.id.btnRChipLikeProperty -> {}
            R.id.btnRChip -> showDialog()
            R.id.closeDialog -> dialog.dismiss()
            R.id.btn_request_permission -> { Toast.makeText(context, "Me clicleaste UwU", Toast.LENGTH_SHORT).show() }
            R.id.btn_emergency -> { Toast.makeText(context, "Me clicleaste UnU", Toast.LENGTH_SHORT).show() }
            else -> {}
        }
}
