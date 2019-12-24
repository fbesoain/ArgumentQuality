package cl.fbesoain.argumentquality


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_motivo_evaluacion.*
import messages.fbesoain.cl.emessage.api.API

class MotivoEvaluacionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motivo_evaluacion)

        val messageID = intent.getStringExtra("messageID")
        val puntuation = intent.getFloatExtra("puntuation", 0.0f)
        println("MESSAGE $messageID - PUNTUATION $puntuation")

        btnSend.setOnClickListener{
            if (tietReason.toString().isBlank())
                Toast.makeText(applicationContext, "Por favor ingrese el motivo de su clasificación", Toast.LENGTH_SHORT).show()
            else {
                API().sendClassification(applicationContext, messageID, puntuation, tietReason.toString())
                Toast.makeText(applicationContext, "Muchas gracias por su evaluación", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
