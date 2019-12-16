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
import com.example.puppr.databinding.FragmentUserPrefSetupBinding
import kotlinx.android.synthetic.main.fragment_user_pref_setup.*

/**
 * A simple [Fragment] subclass.
 */
class userPrefSetupFragment : Fragment() {
    private lateinit var binding: FragmentUserPrefSetupBinding
    private lateinit var userVM: UserViewModel
    private val TAG = "userSetupPref"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentUserPrefSetupBinding>(
            inflater,
            R.layout.fragment_user_pref_setup, container, false
        )
        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        binding.submitButton.setOnClickListener{view:View->

            var checks = arrayOf(false,false,false)

            if(!edit_name.text?.toString().equals("")){
                checks[0] = true
            }
            if(!address_text?.text?.toString().equals("")){
                checks[1] = true
            }
            try{
                if(phone_number.text.toString().matches(Regex("^[0-9]{10,12}\$"))) {
                    checks[2] = true
                }
            }
            catch (e: NumberFormatException){
                checks[2] = false
            }

            if(checks[0] && checks[1] && checks[2]){
                Log.d(TAG, "Successful account creation for user: "+phone_number.text?.toString()?.toInt())
                userVM.user.name = edit_name.text?.toString()
                userVM.user.address = address_text.text?.toString()
                userVM.user.phone = phone_number.text?.toString()?.toInt()
                userVM.user.bio = bio_input.text?.toString()
                userVM.auth.createUserWithEmailAndPassword(userVM.tempEmail, userVM.tempPassword)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) return@addOnCompleteListener
                        else {
                            Log.d(TAG, "Successful account creation for user ${it.result?.user?.uid}")
                            userVM.userID = it.result?.user?.uid
                            val user = hashMapOf(
                                "address" to userVM.user.address,
                                "agePrefHigh" to null,
                                "agePrefLow" to null,
                                "uploadedIdPhoto" to false,
                                "email" to userVM.tempEmail,
                                "name" to userVM.user.name,
                                "bio" to userVM.user.bio,
                                "phone" to userVM.user.phone,
                                "preferredBreeds" to arrayListOf(null),
                                "dislikedDogs" to arrayListOf(null),
                                "likedDogs" to arrayListOf(null)
                            )
                            userVM.database.collection("users").document(userVM.userID.toString())
                                .set(user)
                                .addOnSuccessListener {
                                    Log.d(
                                        TAG,
                                        "DocumentSnapshot successfully written!"
                                    )
                                    view.findNavController().navigate(R.id.action_userPrefSetupFragment_to_clientSavedDogs)
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
                //add userVM.user information (put data in viewModel)

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
