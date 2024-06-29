package com.example.hublss.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.data.datasource.local.EmailDataImpl
import com.example.hublss.databinding.FragmentMainBinding
import com.example.hublss.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAircraft.setOnClickListener {
            val emailData = EmailDataImpl(
                sendTo = binding.etRecepient.text.toString(),
                name = "Triad Mail",
                replyTo = "alfred.nainggolan00@gmail.com",
                isHtml = false,
                title = binding.etSubject.text.toString(),
                body = binding.etMessage.text.toString()
            )
            viewModel.sendEmail(emailData)
        }

        viewModel.sendEmailResult.observe(viewLifecycleOwner) { result ->
            result.onSuccess {
                Toast.makeText(context, "Email sent successfully", Toast.LENGTH_SHORT).show()
            }.onFailure {
                Toast.makeText(context, "Failed to send email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
