package com.minding.aidchips

import android.content.Context
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class DataBase
{
    private val HOSTNAME: String = "https://service.aidchips.tkDoc
                val RECEIVEDP = "/add/ReceivedPermission"
            }
        }

        interface Get {
            companion object {
                val USER = "/get/user"
                val ALERT = "/get/alert"
                val GIVENP = "/get/Permission"
                val RECEIVEDP = "/get/ReceivedPermission"
            }
        }

        interface Del {
            companion object {
                val USER = "/del/USER"
                val ALERT = "/del/alert"
                val GIVENP = "/del/Permission"
                val RECEIVEDP = "/del/ReceivedPermission"
            }
        }

        interface Upd {
            companion object {
                val USER = "/upd/USER"
                val ALERT = "/upd/alert"
                val GIVENP = "/upd/Permission"
                val RECEIVEDP = "/upd/ReceivedPermission"
            }
        }
    }
    interface Method
    {
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
            .add(object : StringRequest(method, "$HOSTNAME$action", Response.Listener { response ->
                println("respuesta $response")
                callback(if(response != "") response!!.toBoolean() else null)
//                returnable = if (response == "false" || response == "true") response.toBoolean()
//           <                     else JSONObject(response)
            }, Response.ErrorListener { e ->
                Toast.makeText(ctx, e.message, Toast.LENGTH_LONG).show()
            }) { override fun getParams(): MutableMap<String, String> = params })
    }

    @Synchronized fun requestJSON( ctx: Context, action: String, method: Int, params: MutableMap<String, String>, callback: (result: JSONObject?) -> Unit  ) {
        Volley.newRequestQueue(ctx)
            .add(object : StringRequest(method, "$HOSTNAME$action", Response.Listener { response ->
                    callback(if (response != "") JSONObject(response) else null)
            }, Response.ErrorListener { e ->
                Toast.makeText(ctx, e.message, Toast.LENGTH_LONG).show()
            }) { override fun getParams(): MutableMap<String, String> = params })
    }
}