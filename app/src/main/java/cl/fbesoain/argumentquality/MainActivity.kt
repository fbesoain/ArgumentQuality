package cl.fbesoain.argumentquality

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import messages.fbesoain.cl.emessage.api.API


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = baseContext.getSharedPreferences(
            "EvaluacionMensaje", Context.MODE_PRIVATE
        )
        firstRun(sharedPreferences)
        btnSave.setOnClickListener {
            if (etAge.text.isBlank())
                Toast.makeText(applicationContext, "Por favor ingrese su edad", Toast.LENGTH_SHORT).show()
            else {
                sharedPreferences.edit().putString("UID", Settings.Secure.getString(applicationContext.contentResolver,
                    Settings.Secure.ANDROID_ID)).apply()
                sharedPreferences.edit().putString("age", etAge.text.toString()).apply()
                sharedPreferences.edit().putString("gender", sGender.selectedItem.toString()).apply()
                sharedPreferences.edit().putString("lastMessageID", "").apply()
                sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
                sharedPreferences.edit().putBoolean("userExist", true).apply()
                API().sendUserToServer(applicationContext)
                startActivity(Intent(applicationContext, EvaluarMensajeActivity::class.java))
                finish()
            }
        }
    }

    private fun firstRun(sharedPreferences: SharedPreferences) {
        val userExist = sharedPreferences.getBoolean("userExist", false)
        val isFirstRun = sharedPreferences.getBoolean("isFirstRun", true)
        if (userExist && isFirstRun)
            sharedPreferences.edit().putBoolean("isFirstRun", false).apply()
        else if (!isFirstRun && userExist) {
            startActivity(Intent(applicationContext, EvaluarMensajeActivity::class.java))
            finish()
        }
    }
}

