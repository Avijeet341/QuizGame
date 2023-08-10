package com.avi.funquizgame

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.avi.funquizgame.databinding.ActivityLoginQuizBinding
import com.google.android.gms.auth.api.identity.SignInPassword
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginQuizActivity: AppCompatActivity() {
    lateinit var loginQuizBinding: ActivityLoginQuizBinding

    val auth:FirebaseAuth=FirebaseAuth.getInstance()
    lateinit var googleSignInClient:GoogleSignInClient

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginQuizBinding=ActivityLoginQuizBinding.inflate(layoutInflater)
        val view=loginQuizBinding.root
        setContentView(view)

        val textofGoogleButton=loginQuizBinding.btnGoogleSignin.getChildAt(0) as TextView
        textofGoogleButton.text="Continue With Google"
        textofGoogleButton.setTextColor(Color.BLACK)
        textofGoogleButton.textSize=18F

        //register
        registerActivityForGoogleSignIn()

        loginQuizBinding.buttonsign.setOnClickListener {
                val userEmail= loginQuizBinding.editTextLoginEmail.text.toString()
                val userPassword=loginQuizBinding.editTextLoginPassword.text.toString()

                 signInUser(userEmail,userPassword)

        }
        loginQuizBinding.btnGoogleSignin.setOnClickListener {
               signInGoogle()
        }

        loginQuizBinding.textViewSignup.setOnClickListener {
            val intent= Intent(this@LoginQuizActivity,SignUpQuizActivity::class.java)
            startActivity(intent)
        }
        loginQuizBinding.textViewForgotPassword.setOnClickListener {
                 val intent=Intent(this@LoginQuizActivity,ForgotPasswordActivity::class.java)
                  startActivity(intent)
        }
    }



    fun signInUser(userEmail:String,userPassword:String){
        auth.signInWithEmailAndPassword(userEmail,userPassword).addOnCompleteListener{task->

            if(task.isSuccessful){
                Toast.makeText(applicationContext,"Welcome to Quiz Game",Toast.LENGTH_SHORT).show()
                val intent=Intent(this@LoginQuizActivity,MainActivity::class.java)
                startActivity(intent)
            }
            else{
                Toast.makeText(applicationContext,task.exception?.localizedMessage,Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onStart() {
        super.onStart()
        val user=auth.currentUser
        //if user is a member already or signed before user value will not be null
        if(user!=null){
            //this process take place at the same time when user enter the email and password and remember it until user again open the app
            Toast.makeText(applicationContext,"Welcome to Quiz Game",Toast.LENGTH_SHORT).show()
            val intent=Intent(this@LoginQuizActivity,MainActivity::class.java)
            startActivity(intent)
        }
    }
    private fun signInGoogle() {
         val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
             .requestIdToken("533633517315-qnpt60bpc081cjb1ti3pndem39ofrkk1.apps.googleusercontent.com")
             .requestEmail().build()//user will choose email from there phones

        googleSignInClient=GoogleSignIn.getClient(this,gso)

        signIn()
    }

    private fun signIn() {
         val signInIntent:Intent=googleSignInClient.signInIntent
        activityResultLauncher.launch(signInIntent)
    }

    private fun registerActivityForGoogleSignIn(){
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback {result->
                // captured the result entered by the user in google signIn page

                val resultCode=result.resultCode
                val data=result.data

                if(resultCode== RESULT_OK && data!=null){
                    val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

                    firebaseSignInWithGoogle(task)
                }

            })
    }

    private fun firebaseSignInWithGoogle(task: Task<GoogleSignInAccount>) {

        try {
            val account:GoogleSignInAccount=task.getResult(ApiException::class.java)
            Toast.makeText(applicationContext,"Welcome To Quiz Game 💕🎇🎉🎊",Toast.LENGTH_SHORT).show()
            //now sigin is done using google btn close this page and open main act using intent
             val intent=Intent(this,MainActivity::class.java)
             startActivity(intent)
            finish()
            firebaseGoogleAccount(account)
        }
        catch (e:ApiException){
            Toast.makeText(applicationContext,e.localizedMessage?.toString(),Toast.LENGTH_SHORT).show()
        }


    }

    private fun firebaseGoogleAccount(account: GoogleSignInAccount) {

        //this will help us find out which device is logged in specially idToken and we registered the authenticated device of the person logged into the system using idtoken
          val authCredential= GoogleAuthProvider.getCredential(account.idToken,null)
          auth.signInWithCredential(authCredential).addOnCompleteListener {task->

          if(task.isSuccessful){
              //retrive the user data if login is successful
//              val user=auth.currentUser

          }else{

          }

          }

    }


}