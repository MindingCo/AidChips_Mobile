package com.minding.aidchips

import android.app.Activity
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.experimental.and

class ServiceReading : Service()
{
    private var nfcAdapter: NfcAdapter? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onCreate() {
//        enableForegroundDispatchSystem()
    }

    override fun onDestroy()
    {
        disableForegroundDispatchSystem()
        super.onDestroy()
        System.out.println("El servicio a Terminado")
    }
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int
    {
        Toast.makeText(this, "hola", Toast.LENGTH_LONG).show()
        return START_STICKY_COMPATIBILITY
    }

    private fun readTextFromMessage(ndefMessage: NdefMessage) {
        val ndefRecords = ndefMessage.records

        if (ndefRecords != null && ndefRecords.isNotEmpty()) {

            val ndefRecord = ndefRecords[0]

            val tagContent = getTextFromNdefRecord(ndefRecord)

            Toast.makeText(this, tagContent, Toast.LENGTH_LONG).show()
        } else
            Toast.makeText(this, "No NDEF records found!", Toast.LENGTH_SHORT).show()
    }

    private fun enableForegroundDispatchSystem() {
        val intent = Intent(this, ClientActivity::class.java).addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
        val intentFilters = arrayOf<IntentFilter>()

        nfcAdapter!!.enableForegroundDispatch(Activity(), pendingIntent, intentFilters, null)

    }

    private fun disableForegroundDispatchSystem() {
        nfcAdapter!!.disableForegroundDispatch(Activity())
    }

    private fun formatTag(tag: Tag, ndefMessage: NdefMessage) {
        try {
            val ndefFormatable = NdefFormatable.get(tag)
            if (ndefFormatable == null) {
                Toast.makeText(this, "Tag is not ndef formatable!", Toast.LENGTH_LONG).show()
                return
            }

            ndefFormatable.connect()
            ndefFormatable.format(ndefMessage)
            ndefFormatable.close()

            Toast.makeText(this, "Tag written!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("formatTag", e.message)
        }

    }

    private fun writeNdefMessage(tag: Tag?, ndefMessage: NdefMessage) {
        try {
            if (tag == null) {
                Toast.makeText(this, "Tag object cannot be null!", Toast.LENGTH_SHORT).show()
                return
            }

            val ndef = Ndef.get(tag)

            if (ndef == null) {
                // format tag with the ndef format and writes the message
                formatTag(tag, ndefMessage)
            } else {
                ndef.connect()
                if (!ndef.isWritable) {
                    Toast.makeText(this, "Tag is not writable!", Toast.LENGTH_SHORT).show()
                    ndef.close()
                    return
                }

                ndef.writeNdefMessage(ndefMessage)
                ndef.close()

                Toast.makeText(this, "Tag written!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("writeNefdMessage", e.message)
        }

    }

    private fun createTextRecord(content: String): NdefRecord? {
        try {
            val language: ByteArray = Locale.getDefault().language.toByteArray(charset("UTF-8"))

            val text = content.toByteArray(charset("UTF-8"))
            val languageSize = language.size
            val textLength = text.size
            val payload = ByteArrayOutputStream(1 + languageSize + textLength)

            payload.write((languageSize and 0x1F).toByte().toInt())
            payload.write(language, 0, languageSize)
            payload.write(text, 0, textLength)

            return NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, ByteArray(0), payload.toByteArray())

        } catch (e: UnsupportedEncodingException) {
            Log.e("createTextRecord", e.message)
        }
        return null
    }

    private fun createNdefMessage(content: String): NdefMessage {

        val ndefRecord = createTextRecord(content)

        return NdefMessage(arrayOf(ndefRecord!!))
    }

//    fun tglReadWriteOnClick(view: View) {
//        txtTagContent.setText("")
//    }

    fun getTextFromNdefRecord(ndefRecord: NdefRecord): String? {
        var tagContent: String? = null
        try {
            val payload = ndefRecord.payload
            val textEncoding = if ((payload[0] and 128.toByte()) == 0.toByte()) "UTF-8" else "UTF16"
            val languageSize = payload[0] and 51
            tagContent = String(payload, languageSize + 1, payload.size - languageSize - 1, charset(textEncoding))
        } catch (e: UnsupportedEncodingException) {
            Log.e("getTextFromNdefRecord", e.message, e)
        }

        return tagContent
    }
}