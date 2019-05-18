package com.minding.aidchips

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.view.ContextThemeWrapper
import android.view.View
import android.view.View.*
import android.widget.*
import org.json.JSONObject
import android.content.Context.TELEPHONY_SERVICE
import android.support.v4.content.ContextCompat.getSystemService
import android.telephony.TelephonyManager



class WritingActivity : AppCompatActivity(), OnClickListener
{
    private lateinit var nfcManager: NFCManager

    lateinit var backBtn : ActionBar
    lateinit var toolbar: android.support.v7.widget.Toolbar

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_writing)
        setTitle(R.string.add_data)
        findViewById<android.support.design.widget.CoordinatorLayout>(R.id.writing_layout).background = resources.getDrawable(R.drawable.background_bring_chip_closer, null)

        nfcManager = NFCManager(this)

        if (!nfcManager.nfcAdapter.isEnabled)
            Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show()

        findViewById<Button>(R.id.writing_btn_writing).setOnClickListener(this)
        setupToolbar()
        setupDialog()
    }

    override fun onResume()
    {
        super.onResume()
        nfcManager.enableForegroundDispatchSystem(this, this, WritingActivity::class.java)
    }

    override fun onPause()
    {
        super.onPause()
        nfcManager.disableForegroundDispatchSystem(this)
    }

    override fun onNewIntent(intent: Intent)
    {
        super.onNewIntent(intent)
        if (nfcManager.isNfcIntent(intent))
        {
            if (dialog.isShowing)
            {
                val basicStructure = JSONObject(
"""{
    "propietary": {
        "name": "${findViewById<TextView>(R.id.writing_field_propierty).text}",
        "tel": "${findViewById<TextView>(R.id.writing_field_tel).text}"
    },
    "emergency data": {
        "full name":"${findViewById<TextView>(R.id.writing_field_name).text}",
        "address":"${findViewById<TextView>(R.id.writing_field_address).text}",
        "health insurance":"${findViewById<TextView>(R.id.writing_field_healthInsurance).text}",
        "blood": "${findViewById<Spinner>(R.id.writing_field_blood).selectedItem}",
        "allergies":"${findViewById<TextView>(R.id.writing_field_allergies).text}",
        "medicines":"${findViewById<TextView>(R.id.writing_field_medicines).text}",
        "donor":"${findViewById<Spinner>(R.id.writing_field_donor).selectedItem}",
        "medial notes":"${findViewById<TextView>(R.id.writing_field_medicalNotes).text}"
    },
    "contact data": {

    },
    "personal data": {

    },
    "files":{

    }
}"""
                )
                println("Escribiendo: $basicStructure")

                nfcManager.write(intent, basicStructure.toString(), this)
                dialog.dismiss()
            }
            else
            {
                findViewById<android.support.design.widget.CoordinatorLayout>(R.id.writing_layout).background = null
                findViewById<android.support.constraint.ConstraintLayout>(R.id.writing_container).visibility = VISIBLE
                val chipStr = nfcManager.read(intent)
                if (chipStr != null)
                {
                    val chip = JSONObject(chipStr)
                    val property = chip.getJSONObject("propietary")

                    findViewById<TextView>(R.id.writing_field_propierty).text = property.getString("name")
                    findViewById<TextView>(R.id.writing_field_tel).text = property.getString("tel")

                    if (chip.has("emergency data"))
                    {
                        val contactData = chip.getJSONObject("emergency data")
                        findViewById<EditText>(R.id.writing_field_name).setText(if (contactData.has("full name")) chip.getJSONObject("emergency data").getString("full name") else "")
                        findViewById<EditText>(R.id.writing_field_address).setText(if (contactData.has("address")) chip.getJSONObject("emergency data").getString("address") else "")
                        findViewById<EditText>(R.id.writing_field_healthInsurance).setText(if (contactData.has("health insurance")) chip.getJSONObject("emergency data").getString("health insurance") else "")
                        if (chip.getJSONObject("emergency data").has("blood"))
                        {
                            findViewById<Spinner>(R.id.writing_field_blood).setSelection(
                                when (contactData.getString("blood"))
                                {
                                    "+A" -> 0
                                    "-A" -> 1
                                    "+B" -> 2
                                    "-B" -> 3
                                    "+AB" -> 4
                                    "-AB" -> 5
                                    "+O" -> 6
                                    "-O" -> 6
                                    else -> 0
                                }, true)
                        }
                        findViewById<EditText>(R.id.writing_field_allergies).setText(if (contactData.has("allergies")) chip.getJSONObject("emergency data").getString("allergies") else "")
                        findViewById<EditText>(R.id.writing_field_medicines).setText(if (contactData.has("medicines")) chip.getJSONObject("emergency data").getString("medicines") else "")
                        if (chip.getJSONObject("emergency data").has("donor"))
                        {
                            findViewById<Spinner>(R.id.writing_field_donor).setSelection(
                                when (contactData.getString("donor"))
                                {
                                    "Not" -> 0
                                    "Yes" -> 1
                                    "No" -> 0
                                    "Sí" -> 1
                                    else -> 0
                                }, true)
                        }
                        findViewById<EditText>(R.id.writing_field_medicalNotes).setText(if (contactData.has("medial notes")) chip.getJSONObject("emergency data").getString("medial notes") else "")
                    }
                }
            }
        }
    }


    @SuppressLint("PrivateResource")
    private fun setupToolbar()
    {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        backBtn = supportActionBar!!
        backBtn.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        backBtn.setDisplayHomeAsUpEnabled(true)
    }

    @SuppressLint("MissingPermission", "HardwareIds")
    override fun onClick(v: View)
    {
        when (v.id)
        {
            R.id.writing_btn_writing ->
            {
                dialog.show()
            }
        }
    }
    private fun setupDialog()
    {
        val dialogView: View = inflate(this, R.layout.dialog_text, null)

        dialogView.findViewById<TextView>(R.id.dialog_text).text = getText(R.string.bring_chip)

        val ctw = ContextThemeWrapper(this, R.style.Main)
        val builder = AlertDialog.Builder(ctw)
        builder.setView(dialogView)
        dialog = builder.create()
    }
}
