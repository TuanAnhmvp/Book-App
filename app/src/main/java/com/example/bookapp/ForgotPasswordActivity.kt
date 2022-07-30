package com.example.bookapp

import android.app.ProgressDialog
import android.app.TaskInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import com.example.bookapp.databinding.ActivityForgotPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    companion object{
       private const val TAG = "TAG_PASSWORD"
    }
    //view binding
    private lateinit var binding: ActivityForgotPasswordBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //progress dialog
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init/setup progress dialog
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please wait")
        progressDialog.setCanceledOnTouchOutside(false)

        //handle click, begin password recovery process
        binding.submitBtn.setOnClickListener {
            validateData()
        }

        //handle click, go back
        binding.backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    private var email = ""
    private fun validateData() {
        //get data
        email = binding.emailEt.text.toString().trim()

        //validate data
        if (email.isEmpty()){
            Toast.makeText(this, "Enter email...", Toast.LENGTH_SHORT).show()

        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email pattern...", Toast.LENGTH_SHORT).show()
        }
        else{
            recoverPassword()
        }
    }

    private fun recoverPassword() {
        //show progress
        progressDialog.setMessage("Sending password reset instructions to $email")
        progressDialog.show()

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                //sent
                progressDialog.dismiss()
                Log.d(TAG, "mail: $email")
                Toast.makeText(this, "instructions sent to \n$email", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed to send due to ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}