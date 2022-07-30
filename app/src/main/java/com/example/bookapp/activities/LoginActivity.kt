package com.example.bookapp.activities

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns

import android.widget.Toast
import com.example.bookapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    //viewbinding
    private lateinit var binding: ActivityLoginBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //Progress dialog
    private lateinit var progeressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()

        //init progresss dialog, will show while login user
        progeressDialog = ProgressDialog(this)
        progeressDialog.setTitle("Please wait")
        progeressDialog.setCanceledOnTouchOutside(false)

        //handle click, not have account, goto register screen
        binding.noAccountTv.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        //handle click, begin login
        binding.loginBtn.setOnClickListener {
            validateData()
        /*
        Steps
        1) Input Data
        2) Validate Data
        3) Login - Firebase Auth
        4) Check user type - Firebase Auth
        if User - Move to user dashboard
        if Admin - Move to admin dashboard*/
        }

        binding.forgotTv.setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

    }
    private var email = ""
    private var password = ""

    private fun validateData() {
        // 1) Input Data
        email = binding.emailEt.text.toString().trim()
        password = binding.passwordEt.text.toString().trim()



        //2) Validate Data
        //invalid email pattern
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Invalid email format...", Toast.LENGTH_SHORT).show()
        }
        else if (password.isEmpty()){
            //empty password...
            Toast.makeText(this, "Enter password...", Toast.LENGTH_SHORT).show()
        }
        /*else if (email.isEmpty()){
            //empty email...
            Toast.makeText(this, "Enter Email...", Toast.LENGTH_SHORT).show()
        }*/
        else{
            loginUser()
        }
    }

    private fun loginUser() {
        //3) Login - Firebase Auth
        //show progress
        progeressDialog.setMessage("Logging In...")
        progeressDialog.show()

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                //login success
                checkUser()
            }
            .addOnFailureListener { e->
                //failed login
                progeressDialog.dismiss()
                Toast.makeText(this, "Login failed due to ${e.message}", Toast.LENGTH_SHORT).show()

            }

    }

    private fun checkUser() {
        //4) Check user type - Firebase Auth
        //        if User - Move to user dashboard
        //        if Admin - Move to admin dashboard*/
        progeressDialog.setMessage("Checking User...")
        val firebaseUser = firebaseAuth.currentUser!!
        val ref = FirebaseDatabase.getInstance().getReference("Users")
        ref.child(firebaseUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onDataChange(snapshot: DataSnapshot) {
                    progeressDialog.dismiss()
                    // get user type  user or admin
                    val userType = snapshot.child("userType").value
                    if (userType == "user"){
                        //its user, open user dashboard
                        startActivity(Intent(this@LoginActivity, DashboardUserActivity::class.java))
                        finish()
                    }
                    else if (userType == "admin"){
                        //its admin, open user dashboard
                        startActivity(Intent(this@LoginActivity, DashboardAdminActivity::class.java))
                        finish()
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
    }
}