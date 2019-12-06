package android.technion.fitracker.user.personal

import android.content.Intent
import android.os.Bundle
import android.technion.fitracker.R
import android.technion.fitracker.login.LoginActivity
import android.technion.fitracker.user.User
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(findViewById(R.id.user_toolbar))
        navController = Navigation.findNavController(findViewById(R.id.user_navigation_host))
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        if (auth.currentUser != null) {
            if(auth.currentUser!!.photoUrl != null){
                Glide.with(this) //1
                    .load(auth.currentUser!!.photoUrl)
                    .placeholder(R.drawable.user_avatar)
                    .error(R.drawable.user_avatar)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .transform(CircleCrop())
                    .into(findViewById(R.id.user_avatar))
            }
            val docRef = firestore.collection("users").document(auth.currentUser!!.uid)
            docRef.get().addOnSuccessListener {
                    document ->
                val user = document.toObject(User::class.java)
                findViewById<TextView>(R.id.user_name).text = user?.name ?: "Username"
            }
        }
        findViewById<BottomNavigationView>(R.id.user_bottom_navigation).setOnNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.user_activity_menu, menu)
        return true

    }


    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        navController.popBackStack()
        when (menuItem.itemId) {
            R.id.action_home -> navController.navigate(R.id.homeScreenFragment)
            R.id.action_workouts -> navController.navigate(R.id.workoutsFragment)
            R.id.action_nutrition -> navController.navigate(R.id.nutritionFragment)
            R.id.action_measurements -> navController.navigate(R.id.measurementsFragment)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.user_menu_logout_ac -> {
            FirebaseAuth.getInstance().signOut()
            startLoginActivity()
            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    private fun startLoginActivity() {
        val userHome = Intent(applicationContext, LoginActivity::class.java)
        startActivity(userHome)
        finish()
    }
}
