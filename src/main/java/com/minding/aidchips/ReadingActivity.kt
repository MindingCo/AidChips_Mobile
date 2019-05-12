package com.minding.aidchips

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

class ReadingActivity : AppCompatActivity()
{
    private lateinit var nfcManager: NFCManager

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reading)
        // Check for available NFC Adapter

        nfcManager = NFCManager(this)

        if (nfcManager.isNfcIntent(intent)) {
            Toast.makeText(this, "NFCIntent!", Toast.LENGTH_SHORT).show()
            nfcManager.tryRead(intent, this)
        }

        if (!nfcManager.nfcAdapter.isEnabled)
            Toast.makeText(this, "NFC not available", Toast.LENGTH_LONG).show()
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
            Toast.makeText(this, "NFCIntent!", Toast.LENGTH_SHORT).show()
            nfcManager.tryRead(intent, this)
        }
    }
}
