package messages.fbesoain.cl.emessage.api

import android.content.Context
import android.content.SharedPreferences
import android.provider.Settings
import android.widget.Toast
import messages.fbesoain.cl.emessage.api.models.User
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager

class API {

    companion object {
        //private var SERVER = "http://10.0.3.2" // GENYMOTION
        //private var SERVER = "http://10.0.2.2" // ADV
        private var SERVER = "http://sorte.ga" // SORTE.GA
        //private var SERVER = "http://192.168.1.101" // IP
        private var PORT = 3110
        private var URL: String = "$SERVER:$PORT"
    }

    fun getApiURL(): String {
        return URL
    }

    fun getSharedPreferences(context: Context): SharedPreferences {
        val sharedPreferences = context.getSharedPreferences(
                "EvaluacionMensaje", Context.MODE_PRIVATE
        )
        return sharedPreferences
    }

    fun sendUserToServer(context: Context) {
        val sharedPreferences = getSharedPreferences(context)
        FuelManager.instance.basePath = URL
        val user = User()
        user.uid = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        user.age = sharedPreferences.getString("age", "")
        user.gender = sharedPreferences.getString("gender", "")
        user.token = sharedPreferences.getString("FirebaseToken", "")
        val params = listOf("uid" to user.uid, "age" to user.age, "gender" to user.gender, "token" to user.token)
        println(params)
        Fuel.post("/user/add", params).timeout(5000).timeoutRead(60000).responseJson { _, _, result ->
            println(result)
            result.fold({ d ->
            }, { err ->
                Toast.makeText(context, "Lo sentimos, ha ocurrido un error. Intentelo nuevamente", Toast.LENGTH_SHORT).show()
            })
        }
    }

    fun sendClassification(context: Context, messageID: String, puntuation: Float, reason: String) {
        val sharedPreferences = getSharedPreferences(context)
        FuelManager.instance.basePath = URL
        val params = listOf("user" to Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID), "message" to messageID, "puntuation" to puntuation,
                "reason" to reason)
        println(params)
        Fuel.post("/messageClassification/add", params).timeout(5000).timeoutRead(60000).responseJson { _, _, result ->
            println(result)
            result.fold({ d ->
            }, { err ->
                Toast.makeText(context, "Lo sentimos, ha ocurrido un error. Intentelo nuevamente", Toast.LENGTH_SHORT).show()
            })
        }
    }

}