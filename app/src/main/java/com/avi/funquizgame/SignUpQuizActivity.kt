package com.avi.funquizgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.avi.funquizgame.databinding.ActivityLoginQuizBinding
import com.avi.funquizgame.databinding.ActivitySignUpQuizBinding
import com.google.firebase.auth.FirebaseAuth

class SignUpQuizActivity : AppCompatActivity() {
    lateinit var signUpQuizActivity:ActivitySignUpQuizBinding
    val auth:FirebaseAuth=FirebaseAuth.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        signUpQuizActivity=ActivitySignUpQuizBinding.inflate(layoutInflater)
        val view= signUpQuizActivity.root
        setContentView(view)

        signUpQuizActivity.buttonSignUp.setOnClickListener {
            val email=signUpQuizActivity.editTextSignUpEmail.text.toString()
            val  password=signUpQuizActivity.editTextSignUpPassword.text.toString()
            signUpWithFirebase(email,password)
        }
    }


    fun signUpWithFirebase(email:String , password:String){
        signUpQuizActivity.progressBarSignup.visibility= View.VISIBLE
        signUpQuizActivity.buttonSignUp.isClickable=false


        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{task->

            if(task.isSuccessful){
                Toast.makeText(applicationContext,"Your account has been created. ✅👍😍", Toast.LENGTH_SHORT).show()
                finish()
                signUpQuizActivity.progressBarSignup.visibility= View.INVISIBLE
                signUpQuizActivity.buttonSignUp.isClickable=true
            }
            else{
                Toast.makeText(applicationContext,task.exception?.localizedMessage+"->"+"😭😭😭😢",
                    Toast.LENGTH_SHORT).show()
            }

        }
    }
}