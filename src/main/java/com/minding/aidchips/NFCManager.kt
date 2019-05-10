package com.minding.aidchips

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import android.util.Log
import android.widget.Toast
import java.io.ByteArrayOutputStream
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.experimental.and

class NFCManager(private var context: Context, private var activity: Activity, var nfcAdapter: NfcAdapter)
{
    fun readNFC(intent: Intent)
    {
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG))
        {
            // Esta parte es de la lectura del chip
            val parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            if (parcelables != null && parcelables.isNotEmpty())
                readTextFromMessage(parcelables[0] as NdefMessage)
            else
                Toast.makeText(context, "No NDEF messages found!", Toast.LENGTH_SHORT).show()
        }
    }
    fun writeNFC(intent: Intent, content: String)
    {
        if (intent.hasExtra(NfcAdapter.EXTRA_TAG))
        {
            // Esta es la parte de la escritura del chip
            val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)

            val ndefMessage = createNdefMessage(content)

            writeNdefMessage(tag, ndefMessage)
        }
    }
    fun getNSerial(intent: Intent): String
    {
        val tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID)
        var hexdump = String()
        for (i in 0 until tagId.size) {
            var x = Integer.toHexString(tagId[i].toInt() and 0xff)
            if (x.length == 1) {
                x = "0$x"
            }
            hexdump += x
        }
        return hexdump
    }

    fun enableForegroundDispatchSystem() =
        nfcAdapter.enableForegroundDispatch(
            activity,
            PendingIntent.getActivity(context, 0, Intent().addFlags(Intent.FLAG_RECEIVER_REPLACE_PENDING), 0),
            arrayOf<IntentFilter>(),
            null )

    fun disableForegroundDispatchSystem() =
        nfcAdapter.disableForegroundDispatch(activity)

    private fun readTextFromMessage(ndefMessage: NdefMessage)
    {
        val ndefRecords = ndefMessage.records

        if (ndefRecords != null && ndefRecords.isNotEmpty())
        {
            val ndefRecord = ndefRecords[0]
            val tagContent = getTextFromNdefRecord(ndefRecord)
            Toast.makeText(context, tagContent, Toast.LENGTH_SHORT).show()
        }
        else
            Toast.makeText(context, "No NDEF records found!", Toast.LENGTH_SHORT).show()
    }

    private fun getTextFromNdefRecord(ndefRecord: NdefRecord): String?
    {
        var tagContent: String? = null
        try
        {
            val payload = ndefRecord.payload
            val textEncoding = if ((payload[0] and 128.toByte()) == 0.toByte()) "UTF-8" else "UTF16"
            val languageSize = payload[0] and 51
            tagContent = String(payload, languageSize + 1, payload.size - languageSize - 1, charset(textEncoding))
        } catch (e: UnsupportedEncodingException) {
            Log.e("getTextFromNdefRecord", e.message, e)
        }

        return tagContent
    }

    private fun writeNdefMessage(tag: Tag?, ndefMessage: NdefMessage) {
        try {
            if (tag == null) {
                Toast.makeText(context, "Tag object cannot be null!", Toast.LENGTH_SHORT).show()
                return
            }

            val ndef = Ndef.get(tag)

            if (ndef == null) {
                // format tag with the ndef format and writes the message
                formatTag(tag, ndefMessage)
            } else {
                ndef.connect()
                if (!ndef.isWritable) {
                    Toast.makeText(context, "Tag is not writable!", Toast.LENGTH_SHORT).show()
                    ndef.close()
                    return
                }

                ndef.writeNdefMessage(ndefMessage)
                ndef.close()

                Toast.makeText(context, "Tag written!", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Log.e("writeNefdMessage", e.message)
        }

    }

    private fun createTextRecord(content: String): NdefRecord?
    {
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

    private fun createNdefMessage(content: String): NdefMessage =
        NdefMessage(arrayOf(createTextRecord(content)!!))

    private fun formatTag(tag: Tag, ndefMessage: NdefMessage) {
        try {
            val ndefFormatable = NdefFormatable.get(tag)
            if (ndefFormatable == null) {
                Toast.makeText(context, "Tag is not ndef formatable!", Toast.LENGTH_LONG).show()
                return
            }

            ndefFormatable.connect()
            ndefFormatable.format(ndefMessage)
            ndefFormatable.close()

            Toast.makeText(context, "Tag written!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Log.e("formatTag", e.message)
        }
    }
}