package com.example.puppr


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.puppr.databinding.FragmentUserSettingsBinding

/**
 * A simple [Fragment] subclass.
 */
class userSettingsFragment : Fragment() {

    private lateinit var binding: FragmentUserSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentUserSettingsBinding>(
            inflater,
            R.layout.fragment_login, container, false
        )
        return binding.root
    }


}
