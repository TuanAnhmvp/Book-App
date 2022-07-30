package com.example.bookapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.bookapp.databinding.ActivityDashboardUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.collections.ArrayList

class DashboardUserActivity : AppCompatActivity() {
    //view binding
    private lateinit var binding: ActivityDashboardUserBinding

    //firebase auth
    private lateinit var firebaseAuth: FirebaseAuth

    //
    private lateinit var categoryArrayList: ArrayList<ModelCategory>
    private lateinit var viewPagerAdapter: ViewPagerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

        //
        setupWithViewPageAdapter(binding.viewPager)
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.logoutBtn.setOnClickListener {
            firebaseAuth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupWithViewPageAdapter(viewPager: ViewPager){
        viewPagerAdapter = ViewPagerAdapter(
            supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT,
            this)
        //init list
        categoryArrayList = ArrayList()

        //load categories from db
        val ref = FirebaseDatabase.getInstance().getReference("Categories")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                //clear list
                categoryArrayList.clear()

                //load some static categories
                //add data to models
                val modeAll = ModelCategory("01", "All", 1, "")
                val modeMostViewed = ModelCategory("01", "Most Viewed", 1, "")
                val modeMostDownloaded = ModelCategory("01", "Most Downloaded", 1, "")

                //add to list
                categoryArrayList.add(modeAll)
                categoryArrayList.add(modeMostViewed)
                categoryArrayList.add(modeMostDownloaded)
                //add to viewPagerAdapter
                viewPagerAdapter.addFragment(
                    BooksUserFragment.newInstance(
                        "${modeAll.id}",
                        "${modeAll.category}",
                        "${modeAll.uid}"
                    ), modeAll.category
                )

                //add to viewPagerAdapter
                viewPagerAdapter.addFragment(
                    BooksUserFragment.newInstance(
                        "${modeMostViewed.id}",
                        "${modeMostViewed.category}",
                        "${modeMostViewed.uid}"
                    ), modeMostViewed.category
                )

                //add to viewPagerAdapter
                viewPagerAdapter.addFragment(
                    BooksUserFragment.newInstance(
                        "${modeMostDownloaded.id}",
                        "${modeMostDownloaded.category}",
                        "${modeMostDownloaded.uid}"
                    ), modeMostDownloaded.category
                )

                //refresh list
                viewPagerAdapter.notifyDataSetChanged()
                //load from firebase db
                for (ds in snapshot.children){
                    //get data in model
                    val mode = ds.getValue(ModelCategory::class.java)
                    //add to list
                    categoryArrayList.add(mode!!)
                    //add to viewPagerAdapter
                    viewPagerAdapter.addFragment(
                        BooksUserFragment.newInstance(
                            "${mode.id}",
                            "${mode.category}",
                            "${mode.uid}"
                        ), mode.category
                    )
                    //refresh list
                    viewPagerAdapter.notifyDataSetChanged()
                }
            }

            override fun onCancelled(error: DatabaseError) {


            }

        })

        //setup adapter to view pager
        viewPager.adapter = viewPagerAdapter
    }


    class ViewPagerAdapter(fm: FragmentManager, behavior: Int, context: Context): FragmentPagerAdapter(fm, behavior){

        //holds list of fragments . new instance of same fragment for each category
        private val fragmentsList: ArrayList<BooksUserFragment> = ArrayList()
        //list of titles of categories, for tabs
        private val fragmentTitleList: ArrayList<String> = ArrayList()
        private val context: Context
        init {
            this.context = context
        }

        override fun getCount(): Int {
            return fragmentsList.size
        }

        override fun getItem(position: Int): Fragment {
            return fragmentsList[position]
        }

        override fun getPageTitle(position: Int): CharSequence{
            return fragmentTitleList[position]
        }

        public fun addFragment(fragment: BooksUserFragment, title: String){

            //add fragment that will be passed as parameter in fragmentList
            fragmentsList.add(fragment)
            //add title that will be passed as parameter
            fragmentTitleList.add(title)
        }


    }

    private fun checkUser() {
        //get current user
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null){
            //not logged in, user can stay in userdashboard without login too
            binding.subTitleTv.text = "Not Logged In"
        }
        else{
            //logged in, get and show user info
            val email = firebaseUser.email
            //set to textview of toolbar
            binding.subTitleTv.text = email
        }
    }
}