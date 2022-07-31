package com.example.bookapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.example.bookapp.adapter.AdapterCategory
import com.example.bookapp.databinding.ActivityDashboardAdminBinding
import com.example.bookapp.models.ModelCategory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DashboardAdminActivity : AppCompatActivity() {
    //view binding
    private lateinit var binding: ActivityDashboardAdminBinding
    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //arraylist to hold categories
    private lateinit var categoryArrayList: ArrayList<ModelCategory>
    //adapter
    private lateinit var adapterCategory: AdapterCategory

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDashboardAdminBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //init firebase Auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
        // load categories
        loadCategories()

        //search
        binding.searchEt.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //called as and when user type anything
                try {
                    adapterCategory.filter.filter(s)
                }
                catch (e: Exception){
                    //Toast.makeText(this@DashboardAdminActivity, "${e.message}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })

        //handle click, logut
        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            checkUser()
        }

        //handle click, start add category page
        binding.addCategoryBtn.setOnClickListener {
            startActivity(Intent(this@DashboardAdminActivity, CategoryAddActivity::class.java))
        }

        //handle click, start add pdf page
        binding.addPdfFab.setOnClickListener{
            startActivity(Intent(this@DashboardAdminActivity, PdfAddActivity::class.java))

        }

        //handle click, open profile
        binding.profileBtn.setOnClickListener {
            startActivity(Intent(this@DashboardAdminActivity, ProfileActivity::class.java))
        }
    }

    private fun loadCategories() {
        //init arraylist
        categoryArrayList = ArrayList()
        //get all categories from firebase database, Firedatabase>Categories
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list before starting adding data into it
                categoryArrayList.clear()
                for (ds in snapshot.children){
                    //get data as model
                    val model = ds.getValue(ModelCategory::class.java)
                    //add to arraylist
                    categoryArrayList.add(model!!)
                }
                //setup adapter
                adapterCategory = AdapterCategory(this@DashboardAdminActivity, categoryArrayList)
                //set adapter to recycleview
                binding.categoriesRv.adapter = adapterCategory
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun checkUser() {
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            //not logged in tro lai main screen
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        else{
            //logged in, get and show user info
            val email = firebaseUser.email
            //set to textview of toolbar
            binding.subTitleTv.text = email
        }

    }


}