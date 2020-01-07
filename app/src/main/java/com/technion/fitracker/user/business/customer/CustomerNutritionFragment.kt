package com.technion.fitracker.user.business.customer


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import com.technion.fitracker.R
import com.technion.fitracker.adapters.nutrition.NutritionFireStoreAdapter
import com.technion.fitracker.models.CustomerDataViewModel
import com.technion.fitracker.models.nutrition.NutritionFireStoreModel
import com.technion.fitracker.user.personal.nutrition.NutritionAddMealActivity

/**
 * A simple [Fragment] subclass.
 */
class CustomerNutritionFragment : Fragment(), View.OnClickListener{
    private lateinit var mAuth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore
    lateinit var fab: ExtendedFloatingActionButton
    lateinit var placeholder: TextView
    private lateinit var viewModel: CustomerDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = activity?.run{
            ViewModelProviders.of(this)[CustomerDataViewModel::class.java]
        } ?: throw Exception("Invalid Fragment, Customer Nutrition Menu")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_nutrion_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        fab = view.findViewById(R.id.customer_nutrition_fab)
        fab.setOnClickListener(this)
        fab.animation = AnimationUtils.loadAnimation(context!!, R.anim.scale_in_card)
        val query =
            firestore.collection("regular_users").document(viewModel.customerID!!).collection("meals")
                    .orderBy("name", Query.Direction.ASCENDING)
        val options =
            FirestoreRecyclerOptions.Builder<NutritionFireStoreModel>()
                    .setQuery(query, NutritionFireStoreModel::class.java).build()
        val onClickListener = View.OnClickListener {
            val element = it.tag as NutritionFireStoreAdapter.ViewHolder
            val name = element.name.text


            firestore.collection("regular_users").document(viewModel.customerID!!).collection("meals").whereEqualTo("name", name).get()
                    .addOnSuccessListener {
                        val doc = it.first().toObject(NutritionFireStoreModel::class.java)
                        val bundle = bundleOf("list" to doc.meals, "name" to name, "docId" to it.first().id, "userID" to viewModel.customerID)
                        val userHome = Intent(context, NutritionAddMealActivity::class.java)
                        userHome.putExtras(bundle)
                        startActivity(userHome)
                    }.addOnFailureListener {

                    }

        }
        viewModel.nutritionAdapter = NutritionFireStoreAdapter(options, onClickListener, this,context!!,viewModel.customerID)
        viewModel.nutritionRV = view.findViewById<RecyclerView>(R.id.customer_nutrition_rec_view).apply{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        if (fab.isShown) {
                            fab.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fab_go_down))
                            fab.visibility = View.GONE
                        }
                    } else if (dy < 0) {
                        if (!fab.isShown) {
                            fab.visibility = View.VISIBLE
                            fab.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fab_go_up))
                        }
                    }
                }
            })
            adapter = viewModel.nutritionAdapter
        }
        placeholder = view.findViewById(R.id.customer_nutrition_placeholder)
    }

    override fun onStart() {
        super.onStart()
        viewModel.nutritionAdapter?.startListening()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.customer_nutrition_fab -> switchToAddActivity()
        }
    }

    private fun switchToAddActivity() {
        val userHome = Intent(context, NutritionAddMealActivity::class.java)
        userHome.putExtra("userID", viewModel.customerID)
        startActivity(userHome)
    }
}
