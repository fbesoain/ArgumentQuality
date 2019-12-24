package cl.fbesoain.argumentquality

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.android.extension.responseJson
import com.github.kittinunf.fuel.core.FuelManager
import kotlinx.android.synthetic.main.activity_evaluar_mensaje.*
import messages.fbesoain.cl.emessage.api.API
import messages.fbesoain.cl.emessage.api.models.Message
import org.json.JSONObject

class EvaluarMensajeActivity : AppCompatActivity() {

    val message = Message()
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_evaluar_mensaje)

        FuelManager.instance.basePath = API().getApiURL()
        sharedPreferences = baseContext.getSharedPreferences(
                "EvaluacionMensaje", Context.MODE_PRIVATE
        )
        println(sharedPreferences.all)
        getLastMessage()
        btnUpdate.setOnClickListener {
            getLastMessage()
        }
        rbClassification.setOnRatingBarChangeListener { ratingBar, fl, b ->
            if (checkStatusLastMessage())
                Toast.makeText(applicationContext, "El mensaje ya ha sido evaluado!", Toast.LENGTH_SHORT).show()
            else {
                if (ratingBar.rating < 3) {
                    val intent = Intent(applicationContext, MotivoEvaluacionActivity::class.java)
                    intent.putExtra("messageID", this.message._id)
                    intent.putExtra("puntuation", ratingBar.rating)
                    startActivity(intent)
                } else {
                    API().sendClassification(applicationContext, this.message._id, ratingBar.rating, "-")
                    Toast.makeText(applicationContext, "Muchas gracias por su evaluación", Toast.LENGTH_SHORT).show()
                    ratingBar.setIsIndicator(true)
                }
                sharedPreferences.edit().putString("lastRate", ratingBar.rating.toString()).apply()
                println("OK el valor es:"+ratingBar.rating.toString())
            }
        }
    }

    fun getLastMessage() {
        Fuel.get("/message/last").timeout(5000).timeoutRead(60000).responseJson { _, _, result ->
            println(result)
            result.fold({ d ->
                val res = JSONObject(result.get().content)
                println(res)
                message._id = res["_id"].toString()
                message.message = res["message"].toString()
            }, { err ->
            })
            if (sharedPreferences.getString("lastMessageID", "") != message._id) {
                sharedPreferences.edit().putString("lastMessageID", message._id).apply()
                rbClassification.numStars = 0
                sharedPreferences.edit().putString("lastRate","").apply()
                rbClassification.setIsIndicator(false)
            }
            if (sharedPreferences.getString("lastMessageID", "").equals(message._id)) {
                val rate = sharedPreferences.getString("lastRate", "")
                if (rate != "") {
                    rbClassification.rating = rate!!.toFloat()
                    rbClassification.setIsIndicator(true)
                }else{
                    rbClassification.setIsIndicator(false)
                }
            }
            tvMessage.text = message.message
        }
    }

    fun checkStatusLastMessage() : Boolean {
        Fuel.get("/message/last").timeout(5000).timeoutRead(60000).responseJson { _, _, result ->
            result.fold({ d ->
                val res = JSONObject(result.get().content)
                message._id = res["_id"].toString()
            }, { err ->
            })}
            if (sharedPreferences.getString("lastMessageID", "").equals(message._id)
                    && sharedPreferences.getString("lastRate", "").equals(""))
                return false
        return true
    }
}