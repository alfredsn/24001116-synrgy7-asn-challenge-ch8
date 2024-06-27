package com.example.hublss.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.data.repository.MainRepositoryImpl
import com.example.domain.datastore.DataStoreManager
import com.example.domain.usecase.LoginUseCase
import com.example.domain.usecase.LogoutUseCase
import com.example.hublss.R
import com.example.hublss.databinding.FragmentLoginBinding
import com.example.hublss.factory.ViewModelFactory
import com.example.hublss.viewmodel.LoginViewModel
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataStoreManager = DataStoreManager

        val factory = ViewModelFactory(
            requireActivity().application,
            LoginUseCase(dataStoreManager),
            LogoutUseCase(dataStoreManager),
            MainRepositoryImpl()
        )
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        lifecycleScope.launch {
            dataStoreManager.getLoginStatus(requireContext()).collect { isLoggedIn ->
                if (isLoggedIn && findNavController().currentDestination?.id == R.id.loginFragment) {
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment)
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString()
            val password = binding.etPassword.text.toString()

            loginViewModel.validateLogin(requireContext(), username, password, {
                if (findNavController().currentDestination?.id == R.id.loginFragment) {
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(R.id.loginFragment, true)
                        .build()
                    findNavController().navigate(R.id.action_loginFragment_to_mainFragment, null, navOptions)
                }
            }, {
                Toast.makeText(requireContext(), "Invalid username or password", Toast.LENGTH_SHORT).show()
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}