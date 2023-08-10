package com.avi.funquizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.avi.funquizgame.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var forgotPasswordBinding: ActivityForgotPasswordBinding

    val auth:FirebaseAuth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        forgotPasswordBinding=ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view=forgotPasswordBinding.root
        setContentView(view)

        forgotPasswordBinding.buttonReset.setOnClickListener {
            val userEmail=forgotPasswordBinding.editTextForgotEmail.text.toString()

            auth.sendPasswordResetEmail(userEmail).addOnCompleteListener {task->

            if (task.isSuccessful){
                Toast.makeText(applicationContext,"We sent a password reset mail to your email address ✅✅😌",Toast.LENGTH_SHORT).show()
                finish()
            }
                else{
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()

            }

            }
        }
    }
}