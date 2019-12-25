package android.technion.fitracker.user.personal.workout

import android.content.Intent
import android.os.Bundle
import android.technion.fitracker.R
import android.technion.fitracker.databinding.ActivityWorkoutStarterBinding
import android.technion.fitracker.models.workouts.WorkoutStartViewModel
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment


class WorkoutStarter : AppCompatActivity() {
    enum class ResultCodes {
        EDIT, DELETE, RETURN
    }

    private lateinit var navController: NavController

    private lateinit var viewModel: WorkoutStartViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this)[WorkoutStartViewModel::class.java]
        val binding =
            DataBindingUtil.setContentView<ActivityWorkoutStarterBinding>(this, R.layout.activity_workout_starter)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        navController = Navigation.findNavController(findViewById(R.id.workout_starter_nav_host))
        viewModel.workoutID.value = intent.getStringExtra("workoutID")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            ResultCodes.EDIT.ordinal -> {
                viewModel.workoutExercises.value?.clear()
                val navHostFragment: NavHostFragment? = supportFragmentManager.findFragmentById(R.id.workout_starter_nav_host) as NavHostFragment?
                (navHostFragment!!.childFragmentManager.fragments[0] as WorkoutStartScreen).extractWorkoutFromDB(this)
            }
            ResultCodes.DELETE.ordinal -> {
                this.finish()
            }
            ResultCodes.RETURN.ordinal -> {

            }

        }
    }


}