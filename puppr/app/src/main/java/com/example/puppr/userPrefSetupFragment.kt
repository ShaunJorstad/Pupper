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
import com.example.puppr.databinding.FragmentSettingsBinding
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
//            Toast.makeText(
//                context, edit_name.hint?.toString(), Toast.LENGTH_LONG).show()
            if(!edit_name.text?.toString().equals(edit_name.hint?.toString()) && !edit_name.text?.toString().equals("")){
                checks[0] = true
            }
            if(address_text.text?.toString().equals(address_text.hint?.toString()) && !address_text?.toString().equals("")){
                checks[1] = true
            }
            if(/*phone_number.text?.toString()?.toInt() !=null &&*/ phone_number.text?.toString().equals(phone_number.hint?.toString())) {
                checks[2] = true
            }
            if(checks[0] && checks[1] && checks[3]){
                userVM.auth.createUserWithEmailAndPassword(userVM.tempEmail, userVM.tempPassword)
                    .addOnCompleteListener {
                        if (!it.isSuccessful) return@addOnCompleteListener
                        else {
                            Log.d(TAG, "Successful account creation for user ${it.result?.user?.uid}")
                            userVM.userID = it.result?.user?.uid
                            val user = hashMapOf(
                                "address" to null,
                                "agePrefHigh" to null,
                                "agePrefLow" to null,
                                "uploadedIdPhoto" to false,
                                "email" to userVM.tempEmail,
                                "name" to null,
                                "bio" to null,
                                "phone" to null,
                                "prefferedBreeds" to arrayListOf(null),
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
                for(i in 1 until checks.size){
                    Toast.makeText(
                        context, "Field $i does not fit the necessary format", Toast.LENGTH_LONG).show()
                }
            }
        }
        return binding.root
    }
}
