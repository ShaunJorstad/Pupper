package com.example.puppr


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.puppr.databinding.FragmentShelterPrefSetupBinding
import kotlinx.android.synthetic.main.fragment_shelter_pref_setup.*
import kotlinx.android.synthetic.main.fragment_user_pref_setup.*

/**
 * A simple [Fragment] subclass.
 */
class shelterPrefSetupFragment : Fragment() {
    private lateinit var binding: FragmentShelterPrefSetupBinding
    private lateinit var userVM: UserViewModel
    private val TAG = "shelterSetupPref"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentShelterPrefSetupBinding>(
            inflater,
            R.layout.fragment_shelter_pref_setup, container, false
        )
        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.submitButton.setOnClickListener{view:View->

            var checks = arrayOf(false,false,false,false)

            checks[0] = !shelter_name_text.text?.toString().equals("")

            checks[1] = !s_address_text?.text?.toString().equals("")
            try{
                if(s_phone_number.text.toString().matches(Regex("^[+]?[0-9]{10,12}\$"))) {
                    checks[2] = true
                }
            }
            catch (e: NumberFormatException){
                checks[2] = false
            }
            checks[3] = !shelter_website?.text?.toString().equals("")

            if(checks[0] && checks[1] && checks[2] && checks[3]){
                Log.d(TAG, "Successful account creation for user: "+s_phone_number.text?.toString())
                userVM.shelter.name = shelter_name_text.text?.toString()
                userVM.shelter.address = s_address_text.text?.toString()
                userVM.shelter.phone = s_phone_number.text?.toString()
                userVM.shelter.bio = s_bio_input.text?.toString()
                userVM.shelter.address = s_address_text.text?.toString()
                userVM.shelter.website = shelter_website.text?.toString()

                userVM.auth.createUserWithEmailAndPassword(userVM.tempEmail, userVM.tempPassword)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) return@addOnCompleteListener
                        else {
                            Log.d(TAG, "Successful account creation for user ${it.result?.user?.uid}")
                            userVM.userID = it.result?.user?.uid
                            val shelter = hashMapOf(
                                "address" to userVM.shelter.address,
                                "email" to userVM.tempEmail,
                                "websiteUrl" to userVM.shelter.website,
                                "name" to userVM.shelter.name,
                                "bio" to userVM.shelter.bio,
                                "phone" to userVM.shelter.phone,
                                "dogs" to arrayListOf(null),
                                "photos" to arrayListOf(null)
                            )
                            userVM.database.collection("shelters").document(userVM.userID.toString())
                                .set(shelter)
                                .addOnSuccessListener {
                                    Log.d(
                                        TAG,
                                        "DocumentSnapshot succwessfully written!"
                                    )
                                    view?.findNavController()?.navigate(R.id.action_shelterPrefSetupFragment_to_shelterDogs)
                                }
                                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            context,
                            it.message,
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
            }
            else{
                for(i in 0 until checks.size){
                    if(!checks[i]) {
                        Toast.makeText(
                            context, "Field ${i+1} does not fit the necessary format", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
        return binding.root
    }
}
