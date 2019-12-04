package android.technion.fitracker.login


import android.content.Intent
import android.os.Bundle
import android.technion.fitracker.R
import android.technion.fitracker.user.UserActivity
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth

/**
 * A simple [Fragment] subclass.
 */
class SignUpFragment : Fragment(), View.OnClickListener {

    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    //Fields
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null) {
            startUserActivity()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.sign_up_fragment_sign_up_button).setOnClickListener(this)
        view.findViewById<Button>(R.id.sign_up_fragment_trainer_sign_up_button).setOnClickListener(this)

        emailEditText = view.findViewById(R.id.sign_up_fragment_email)
        passwordEditText = view.findViewById(R.id.sign_up_fragment_pass)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.sign_up_fragment_sign_up_button -> handleEmailSignUp()
            R.id.sign_up_fragment_trainer_sign_up_button -> navController.navigate(
                R.id.action_signUpFragment_to_signUpBusinessFragment
            )
        }
    }

    private fun handleEmailSignUp() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity!!) { task ->
                if (task.isSuccessful) {
                    // Sign up success
                    startUserActivity()
                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun startUserActivity() {
        val userHome = Intent(context!!, UserActivity::class.java)
        startActivity(userHome)
        activity?.finish()
    }
}
