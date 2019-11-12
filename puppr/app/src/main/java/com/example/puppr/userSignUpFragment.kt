package com.example.puppr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.puppr.databinding.FragmentUserSignUpBinding

/**
 * A simple [Fragment] subclass.
 */
class userSignUpFragment : Fragment() {
    private lateinit var binding: FragmentUserSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentUserSignUpBinding>(
            inflater,
            R.layout.fragment_signup_options, container, false
        )
        return binding.root
    }


}
