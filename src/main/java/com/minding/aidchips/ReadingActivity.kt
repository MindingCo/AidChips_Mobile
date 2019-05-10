package com.minding.aidchips

import android.content.Intent
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class ReadingActivity : AppCompatActivity()
{
    private var nfc: NFCManager? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)
        // Check for available NFC Adapter

        nfc = NFCManager(this, this, NfcAdapter.getDefaultAdapter(this))

        if (nfc!!.nfcAdapter.isEnabled)
            nfc!!.readNFC(intent)
        else
            Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show()
    }
    override fun onNewIntent(intent: Intent)
    {
        super.onNewIntent(intent)
        if (nfc!!.nfcAdapter.isEnabled)
            nfc!!.readNFC(intent)
        else
            Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show()
    }

    override fun onResume()
    {
        super.onResume()
        nfc!!.enableForegroundDispatchSystem()
    }

    override fun onPause()
    {
        super.onPause()
        nfc!!.disableForegroundDispatchSystem()
    }

}
