package com.minding.aidchips

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import org.json.JSONObject

class ReadingActivity : AppCompatActivity()
{
    private lateinit var nfcManager: NFCManager
    lateinit var backBtn : ActionBar
    lateinit var toolbar: android.support.v7.widget.Toolbar

    interface typeOfPermit {
        companion object {
            val EMERGENCY = 2
            val INVITED = 1
            val OWNER = 0
        }
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)
        setTitle(R.string.read_data)
        // Check for available NFC Adapter

        nfcManager = NFCManager(this)

        if (!nfcManager.nfcAdapter.isEnabled)
            Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show()
        if (nfcManager.isNfcIntent(intent))
        showData(intent)

        setupToolbar()
    }
    override fun onResume() {
        super.onResume()
        nfcManager.enableForegroundDispatchSystem(this, this, ReadingActivity::class.java)
    }

    override fun onPause() {
        super.onPause()

        nfcManager.disableForegroundDispatchSystem(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        if (nfcManager.isNfcIntent(intent)) {
            showData(intent)
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

    private fun showData(intent: Intent)
    {
        val chip = JSONObject(nfcManager.read(intent))
        val propierty = chip.getJSONObject("propietary")

        findViewById<TextView>(R.id.reading_field_propierty).text = propierty.getString("name")
        findViewById<TextView>(R.id.reading_field_tel).text = propierty.getString("tel")

        if (chip.has("contact data"))
        {
            val contactData = chip.getJSONObject("contact data")
            findViewById<TextView>(R.id.reading_field_name).text = if (contactData.has("full name")) chip.getJSONObject("contact data").getString("full name") else "-"
            findViewById<TextView>(R.id.reading_field_address).text = if (contactData.has("address")) chip.getJSONObject("contact data").getString("address") else "-"
            findViewById<TextView>(R.id.reading_field_healthInsurance).text = if (contactData.has("health insurance")) chip.getJSONObject("contact data").getString("health insurance") else "-"
            findViewById<TextView>(R.id.reading_field_blood).text = if (contactData.has("blood")) chip.getJSONObject("contact data").getString("blood") else "-"
            findViewById<TextView>(R.id.reading_field_allergies).text = if (contactData.has("allergies")) chip.getJSONObject("contact data").getString("allergies") else "-"
            findViewById<TextView>(R.id.reading_field_medicines).text = if (contactData.has("medicines")) chip.getJSONObject("contact data").getString("medicines") else "-"
            findViewById<TextView>(R.id.reading_field_donor).text = if (contactData.has("donor")) chip.getJSONObject("contact data").getString("donor") else "-"
            findViewById<TextView>(R.id.reading_field_medicalNotes).text = if (contactData.has("medial notes")) chip.getJSONObject("contact data").getString("medial notes") else "-"
        }
    }

    private fun typeOfPermit(): Int
    {

    }
}