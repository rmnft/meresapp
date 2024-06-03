package com.example.meresap2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.meresap2.databinding.FragmentInstruction2Binding

class InstructionFragment2 : Fragment() {

    private var _binding: FragmentInstruction2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInstruction2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnStart.setOnClickListener {
            val groupFragment = GroupFragment2.newInstance("ETAPA 02", "GRUPO 06", emptyList())
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, groupFragment)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): InstructionFragment2 {
            return InstructionFragment2()
        }
    }
}