package com.example.puppr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.puppr.databinding.FragmentSettingsBinding
import com.example.puppr.databinding.FragmentUserPrefSetupBinding

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
        return binding.root
    }
}
