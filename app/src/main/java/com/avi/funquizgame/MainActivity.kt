package com.avi.funquizgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.avi.funquizgame.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    lateinit var mainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding=ActivityMainBinding.inflate(layoutInflater)
        val view= mainBinding.root
        setContentView(view)

        supportActionBar?.title="Quiz Game 🎇"

        mainBinding.buttonStartQuiz.setOnClickListener {
            val intent=Intent(this,QuizActivity::class.java)
            startActivity(intent)
        }

        mainBinding.buttonSIgnOut.setOnClickListener {

            //email and password signOut
            FirebaseAuth.getInstance().signOut()


            //google account sign out
            val gso=GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build()

            val googleSignInClient=GoogleSignIn.getClient(this,gso)
            googleSignInClient.signOut().addOnCompleteListener {task->

            if (task.isSuccessful){
                Toast.makeText(applicationContext,"Sign Out is successful✅💕👍",Toast.LENGTH_SHORT).show()
            }

            }



            val intent= Intent(this@MainActivity,LoginQuizActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

//    override fun onBackPressed() {
//        val intent= Intent(this@MainActivity,LoginQuizActivity::class.java)
//        startActivity(intent)
//        finish()
//    }
}