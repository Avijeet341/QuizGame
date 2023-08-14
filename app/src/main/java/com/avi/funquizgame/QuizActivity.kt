package com.avi.funquizgame

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.avi.funquizgame.databinding.ActivityQuizBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.random.Random

class QuizActivity : AppCompatActivity() {
    lateinit var QuizBinding:ActivityQuizBinding

    val database:FirebaseDatabase=FirebaseDatabase.getInstance()
    val databaseReference=database.reference.child("questions")
// database input
    var question=""
    var answerA=""
    var answerB=""
    var answerC=""
    var answerD=""
    var correctAnswer=""
 //  next btn counter
    var questionCount=0
    var questionNumber=0//change set 0--->4
    //user input
    var userAnswer=""
    var userCorrect=0
    var userWrong=0

    //Timer
    lateinit var timer:CountDownTimer
    private val totalTime=25000L
    var timerContinue=false
    var leftTime=totalTime
    // SAVING USER SCORE USING FIRE AUTH OBJ AND USER UID

    val auth:FirebaseAuth=FirebaseAuth.getInstance()
    val user=auth.currentUser
    //store the score in data base using database reference obj

    val ScoreRef=database.reference

    //filtering questions

    val questions=HashSet<Int>()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QuizBinding=ActivityQuizBinding.inflate(layoutInflater)
        val view=QuizBinding.root
        setContentView(view)
        //filtering

        do{
           val number= Random.nextInt(1,11)
            Log.d("number",number.toString())
            questions.add(number)
        } while (questions.size<5)

        Log.d("numberOfQuestions",questions.toString())

//

        gameLogic()
        QuizBinding.buttonNext.setOnClickListener {
            resetTimer()
            gameLogic()
        }

        QuizBinding.buttonFinish.setOnClickListener {
            sendScore()
        }

        QuizBinding.textViewA.setOnClickListener {
            pauseTimer()
          userAnswer="a"

            if(userAnswer==correctAnswer){
                QuizBinding.textViewA.setBackgroundColor(Color.GREEN)
                userCorrect++;
                QuizBinding.textViewCorrect.text=userCorrect.toString()
            }
            else{
                QuizBinding.textViewA.setBackgroundColor(Color.RED)
                userWrong++
                QuizBinding.textViewWrong.text=userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()
        }

        QuizBinding.textViewB.setOnClickListener {
            pauseTimer()
          userAnswer="b"

            if(userAnswer==correctAnswer){
                QuizBinding.textViewB.setBackgroundColor(Color.GREEN)
                userCorrect++;
                QuizBinding.textViewCorrect.text=userCorrect.toString()
            }
            else{
                QuizBinding.textViewB.setBackgroundColor(Color.RED)
                userWrong++
                QuizBinding.textViewWrong.text=userWrong.toString()
                findAnswer()
            }

            disableClickableOptions()
        }

        QuizBinding.textViewC.setOnClickListener {
            pauseTimer()
          userAnswer="c"

            if(userAnswer==correctAnswer){
                QuizBinding.textViewC.setBackgroundColor(Color.GREEN)
                userCorrect++;
                QuizBinding.textViewCorrect.text=userCorrect.toString()
            }
            else{
                QuizBinding.textViewC.setBackgroundColor(Color.RED)
                userWrong++
                QuizBinding.textViewWrong.text=userWrong.toString()
                findAnswer()
            }
            disableClickableOptions()
        }

        QuizBinding.textViewD.setOnClickListener {
            pauseTimer()
          userAnswer="d"

            if(userAnswer==correctAnswer){
                QuizBinding.textViewD.setBackgroundColor(Color.GREEN)
                userCorrect++;
                QuizBinding.textViewCorrect.text=userCorrect.toString()
            }
            else{
                QuizBinding.textViewD.setBackgroundColor(Color.RED)
                userWrong++
                QuizBinding.textViewWrong.text=userWrong.toString()
                findAnswer()
            }
            disableClickableOptions()
        }
    }

    private  fun gameLogic(){
        databaseReference.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                //number of children under questions parent using snapshot obj
                questionCount= snapshot.childrenCount.toInt()
                if(questionNumber<questions.size){
                    //data retrieving method
                    question=snapshot.child(questions.elementAt(questionNumber).toString()).child("q").value.toString()
                    answerA=snapshot.child(questions.elementAt(questionNumber).toString()).child("a").value.toString()
                    answerB=snapshot.child(questions.elementAt(questionNumber).toString()).child("b").value.toString()
                    answerC=snapshot.child(questions.elementAt(questionNumber).toString()).child("c").value.toString()
                    answerD=snapshot.child(questions.elementAt(questionNumber).toString()).child("d").value.toString()
                    correctAnswer=snapshot.child(questions.elementAt(questionNumber).toString()).child("answer").value.toString()

                    QuizBinding.textViewQuestion.text=question
                    QuizBinding.textViewA.text=answerA
                    QuizBinding.textViewB.text=answerB
                    QuizBinding.textViewC.text=answerC
                    QuizBinding.textViewD.text=answerD

                    QuizBinding.progressBarQuiz.visibility=View.INVISIBLE
                    QuizBinding.linearLayoutinfo.visibility=View.VISIBLE
                    QuizBinding.LinearLayoutQuestion.visibility=View.VISIBLE
                    QuizBinding.linearLayoutButtons.visibility=View.VISIBLE

                    startTimer()
                }

                else{
//                    all questions are attempted show dialog window
                    disableClickableOptions()//I DID THIS
//                    Toast.makeText(applicationContext,"You have answered all the questions 💕😌🍿",Toast.LENGTH_SHORT).show()
                    val dialogMessage=AlertDialog.Builder(this@QuizActivity)
                        dialogMessage.setTitle("Quiz Game")
                        dialogMessage.setMessage("Congratulations !!!🎉🎉, \n You  have answered all the questions. Do you want to see the result?")
                        dialogMessage.setCancelable(false)
                        dialogMessage.setPositiveButton("See Result"){dialogWindow,position->

                            sendScore()

                        }
                     dialogMessage.setNegativeButton("Play Again"){dialogWindow,position->
                         val intent=Intent(this@QuizActivity,MainActivity::class.java)
                         startActivity(intent)
                         finish()
                     }
                    dialogMessage.create().show()
                }

   questionNumber++

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext,error.message,Toast.LENGTH_SHORT).show()

            }

        })

        restoreOptions()
    }

    fun findAnswer(){
        //switch case
        when(correctAnswer){
            "a"->QuizBinding.textViewA.setBackgroundColor(Color.GREEN)
            "b"->QuizBinding.textViewB.setBackgroundColor(Color.GREEN)
            "c"->QuizBinding.textViewC.setBackgroundColor(Color.GREEN)
            "d"->QuizBinding.textViewD.setBackgroundColor(Color.GREEN)
        }
    }

    fun disableClickableOptions(){
        QuizBinding.textViewA.isClickable=false
        QuizBinding.textViewB.isClickable=false
        QuizBinding.textViewC.isClickable=false
        QuizBinding.textViewD.isClickable=false
    }

    fun restoreOptions(){
        QuizBinding.textViewA.setBackgroundColor(Color.WHITE)
        QuizBinding.textViewB.setBackgroundColor(Color.WHITE)
        QuizBinding.textViewC.setBackgroundColor(Color.WHITE)
        QuizBinding.textViewD.setBackgroundColor(Color.WHITE)

        QuizBinding.textViewA.isClickable=true
        QuizBinding.textViewB.isClickable=true
        QuizBinding.textViewC.isClickable=true
        QuizBinding.textViewD.isClickable=true
    }

    private fun startTimer(){
        timer=object: CountDownTimer(leftTime,1000){
            override fun onTick(millisUntilFinished: Long) {
                 //timer action every sec millisUntilFinish(Time left until 0sec)

                leftTime=millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                disableClickableOptions()
                 resetTimer()
                updateCountDownText()
                QuizBinding.textViewQuestion.text="Sorry,Time is up!😢 Continue with next question😌💕."
                timerContinue=false

            }
        }.start()
        timerContinue=true
    }

    //ever sec timer will updates it time
    fun updateCountDownText(){
        val remainingTime:Int=(leftTime/1000).toInt()
        QuizBinding.textViewTime.text=remainingTime.toString()

    }
    fun pauseTimer(){
        timer.cancel()
        timerContinue=false
    }
    fun  resetTimer(){
        pauseTimer()
        leftTime=totalTime
        updateCountDownText()
    }


    fun sendScore(){
        user?.let {
            val userUID= it.uid
            ScoreRef.child("scores").child(userUID).child("correct").setValue(userCorrect)
            ScoreRef.child("scores").child(userUID).child("wrong").setValue(userWrong).addOnSuccessListener {
                Toast.makeText(applicationContext,"Scores sent to database successfully ✅✅",Toast.LENGTH_SHORT).show()
                val intent=Intent(this@QuizActivity,ResultActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}