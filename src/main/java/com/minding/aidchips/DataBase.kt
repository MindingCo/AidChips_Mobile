package com.minding.aidchips

import android.content.Context
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class DataBase {
    val HOSTNAME: String = "https://service.aidchips.tk"
    interface Action {
        companion object {
            val LOGIN = "/login"
            val TYPEPERMIT = "/typeofpermit"
        }
        interface Add {
            companion object {
                val USER = "/add/user"
                val CHIP = "/add/chip"
                val ALERT = "/add/notif"
                val PERMIT = "/add/permit"
            }
        }

        interface Get {
            companion object {
                val USER = "/get/user"
                val CHIP = "/get/chip"
                val CHIPS = "/get/chips"
                val NOTIF = "/get/notif"
                val PERMIT = "/get/permit"
                val GIVENP = "/get/givenpermits"
                val RECEIVEDP = "/get/receivedpermits"
            }
        }

        interface Del {
            companion object {
                val USER = "/del/user"
                val CHIP = "/del/chip"
                val ALERT = "/del/notif"
                val PERMIT = "/del/permit"
                val GIVENP = "/del/givenpermission"
                val RECEIVEDP = "/del/receivedpermission"
            }
        }

        interface Upd {
            companion object {
                val USER = "/upd/USER"
                val CHIP = "/upd/chip"
                val ALERT = "/upd/notif"
                val GIVENP = "/upd/Permit"
                val RECEIVEDP = "/upd/ReceivedPermission"
            }
        }
    }
    interface Method {
        companion object {
            val DEPRECATED_GET_OR_POST = -1
            val GET = 0
            val POST = 1
            val PUT = 2
            val DELETE = 3
            val HEAD = 4
            val OPTIONS = 5
            val TRACE = 6
            val PATCH = 7
        }
    }

    @Synchronized fun requestOperation(ctx: Context, action: String, method: Int, params: MutableMap<String, String>, callback: (result: Boolean?) -> Unit )
    {
        Volley.newRequestQueue(ctx)
            .add(object : StringRequest(method, "$HOSTNAME$action", Response.Listener
            { response ->
                println("respuesta $response")
                callback(if(response != "") response!!.toBoolean() else null)
//                returnable = if (response == "false" || response == "true") response.toBoolean()
//           <                     else JSONObject(response)
            }, Response.ErrorListener
            { e ->
                Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show()
                callback(null)
            }) { override fun getParams(): MutableMap<String, String> = params })
    }

    @Synchronized fun requestJSON( ctx: Context, action: String, method: Int, params: MutableMap<String, String>, callback: (result: JSONObject?) -> Unit  )
    {
        Volley.newRequestQueue(ctx)
            .add(object : StringRequest(method, "$HOSTNAME$action", Response.Listener
            { response ->
                callback(if (response != "") JSONObject(response) else null)
            }, Response.ErrorListener
            { e ->
                Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show()
                callback(null)
            }) { override fun getParams(): MutableMap<String, String> = params })
    }

    @Synchronized fun requestArrayJSON( ctx: Context, action: String, method: Int, params: MutableMap<String, String>, callback: (result: JSONArray?) -> Unit  )
    {
        Volley.newRequestQueue(ctx)
            .add(object : StringRequest(method, "$HOSTNAME$action", Response.Listener
            { response ->
                callback(if (response != "") JSONArray(response) else null)
            }, Response.ErrorListener
            { e ->
                Toast.makeText(ctx, e.toString(), Toast.LENGTH_LONG).show()
                callback(null)
            }) { override fun getParams(): MutableMap<String, String> = params })
    }
}