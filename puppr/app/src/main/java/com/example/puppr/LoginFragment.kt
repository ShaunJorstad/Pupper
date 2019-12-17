package com.example.puppr


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.example.puppr.databinding.FragmentLoginBinding
import kotlinx.android.synthetic.main.fragment_login.email_input
import kotlinx.android.synthetic.main.fragment_login.password_input
import android.R.string


/**
 * Fragment that handles user login and account creation
 */
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private lateinit var userVM: UserViewModel
    private var TAG = "userLogIn"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentLoginBinding>(
            inflater,
            R.layout.fragment_login, container, false
        )
        userVM = activity?.run {
            ViewModelProviders.of(this).get(UserViewModel::class.java)
        } ?: throw Exception("Invalid Activity")
        binding.signInSubmitButton.setOnClickListener {
            loginUser(it)
        }
        binding.userSignUpButton.setOnClickListener {
            userVM.userType = "user"
            userVM.tempEmail = email_input.text.toString()
            userVM.tempPassword = password_input.text.toString()
            it.findNavController()
                .navigate(R.id.action_userLoginFragment_to_signUpFragment)
        }
        binding.shelterSignUpButton.setOnClickListener {
            userVM.userType = "shelter"
            userVM.tempEmail = email_input.text.toString()
            userVM.tempPassword = password_input.text.toString()
            it.findNavController()
                .navigate(R.id.action_userLoginFragment_to_signUpFragment)
        }
        binding.forgotPassword.setOnClickListener {
            try {
                userVM.auth.sendPasswordResetEmail(email_input.text.toString())
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                context,
                                "Password reset link sent to: " + email_input.text.toString(),
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
            } catch (error: Exception) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT)
                    .show()
            }

        }

        return binding.root
    }

    /**
     * logs in user with the provided information
     */
    private fun loginUser(view: View) {
        var email = email_input.text.toString()
        var password = password_input.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "please enter text into email and password", Toast.LENGTH_SHORT)
                .show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(context, "password must be longer than 5 characters", Toast.LENGTH_SHORT)
                .show()
            return
        }

        userVM.auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener
                else {
                    userVM.userID = it.result?.user?.uid
                    signInUser()
                }
            }
            .addOnFailureListener {
                Log.d(TAG, "Failed to sign in user: ${it.message}")
                view.hideKeyboard()
                Toast.makeText(context, "invalid login credentials", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * changes fragment if there is a signed in user.
     */
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = userVM.auth.currentUser
        Log.i(TAG, "Current User ID: ${userVM.auth.currentUser}")
        if (currentUser != null) {
            //set user type
            userVM.userID = currentUser.uid
            signInUser()
        }
    }

    /**
     * gets the signed in user's account data, such as liked dogs, disliked dogs, etc... this is all
     * stored in the user View Model
     */
    public fun signInUser() {
        var firestoreUser = userVM.database.collection("users").document(userVM.userID.toString())
        firestoreUser.get()
            .addOnSuccessListener { userDocument ->
                if (userDocument.data != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${userDocument.data}")
                    userVM.userType = "user"
                    //navigate to user fragment view thing
                    //userVM.user.likedDogs = userVM.user.likedDogs?.plusElement("hey shaun")

                    userVM.user.name = userDocument.data?.getValue("name")?.toString()
                    userVM.user.email = userDocument.data?.getValue("email")?.toString()
                    userVM.user.address = userDocument.data?.getValue("address")?.toString()
                    userVM.user.phone = userDocument.data?.getValue("phone").toString()
                    userVM.user.agePrefHigh =
                        userDocument.data?.getValue("agePrefHigh")?.toString()?.toInt()
                    userVM.user.agePrefLow =
                        userDocument.data?.getValue("agePrefLow")?.toString()?.toInt()
                    userVM.user.bio = userDocument.data?.getValue("bio")?.toString()

                    var disLikedDogStr = userDocument.data?.getValue("dislikedDogs")?.toString()
                    disLikedDogStr = disLikedDogStr?.substring(1, disLikedDogStr.length - 1)
                    var DLdogList = disLikedDogStr?.replace(" ", "")!!.split(",").toTypedArray()
                    userVM.user.dislikedDogs = DLdogList.toList()

                    var likedDogStr = userDocument.data?.getValue("likedDogs")?.toString()
                    likedDogStr = likedDogStr?.substring(1, likedDogStr.length - 1)
                    var LdogList = likedDogStr?.replace(" ", "")!!.split(",").toTypedArray()
                    userVM.user.likedDogs = LdogList.toList()

                    var prefDogStr = userDocument.data?.getValue("preferredBreeds")?.toString()
                    prefDogStr = prefDogStr?.substring(1, prefDogStr.length - 1)
                    var prefDogList = prefDogStr?.split(",")?.toTypedArray()
                    userVM.user.preferredBreeds = prefDogList?.toList()

                    view?.findNavController()
                        ?.navigate(R.id.action_userLoginFragment_to_clientPreferences)

                } else {
                    userVM.database.collection("shelters")
                        .document(userVM.userID.toString()).get()
                        .addOnSuccessListener { shelterDocument ->
                            if (shelterDocument.data != null) {
                                userVM.userType = "shelter"
                                userVM.shelter.name =
                                    shelterDocument.data?.getValue("name")?.toString()
                                userVM.shelter.address =
                                    shelterDocument.data?.getValue("address")?.toString()
                                userVM.shelter.phone =
                                    shelterDocument.data?.getValue("phone")?.toString()
//                                userVM.shelter.photos = innerDocument.data?.getValue("photos")
                                userVM.shelter.website =
                                    shelterDocument.data?.getValue("websiteUrl")?.toString()

                                var dogStr = shelterDocument.data?.getValue("dogs")?.toString()
                                dogStr = dogStr?.substring(1, dogStr.length - 1)
                                var dogList = dogStr?.split(",")?.toTypedArray()
                                userVM.shelter.dogs = dogList?.toList()

                                view?.findNavController()
                                    ?.navigate(R.id.action_userLoginFragment_to_shelterDogs)
                            }
                        }
                        .addOnFailureListener { exception ->
                            Log.d(TAG, "get failed with ", exception)
                        }
                }
            }
            .addOnFailureListener { exception ->
                Log.d(TAG, "get failed with ", exception)
            }
    }

    /**
     * hides the keyboard
     */
    fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}
