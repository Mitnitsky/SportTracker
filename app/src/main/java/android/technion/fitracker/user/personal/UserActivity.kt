package android.technion.fitracker.user.personal

import android.os.Bundle
import android.technion.fitracker.R
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        setSupportActionBar(findViewById(R.id.user_toolbar))
        navController = Navigation.findNavController(findViewById(R.id.user_navigation_host))
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
}