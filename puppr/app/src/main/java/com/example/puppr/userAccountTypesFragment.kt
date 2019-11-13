package com.example.puppr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.puppr.databinding.FragmentUserAccountTypesBinding
import com.google.firebase.auth.FirebaseAuth

class userAccountTypesFragment : Fragment() {
    private lateinit var binding: FragmentUserAccountTypesBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        TODO: Check to see if the FirebaseAuth object has a current user instance. If so navigate to the main page before inflating this fragment
        auth = FirebaseAuth.getInstance()
        val binding = DataBindingUtil.inflate<FragmentUserAccountTypesBinding>(
            inflater,
            R.layout.fragment_user_account_types, container, false
        )

        binding.userButton.setOnClickListener{view : View ->
            view.findNavController().navigate(R.id.action_loginFragment_to_signupOptionsFragment)}

        return binding.root
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
//        updateUI(currentUser)
//        TODO: Navigate to settings fragment, passing the current user?
    }
}
