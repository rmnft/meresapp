package com.example.meresap2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.example.meresap2.databinding.FragmentGroupBinding

class GroupFragment : Fragment() {

    private var _binding: FragmentGroupBinding? = null
    private val binding get() = _binding!!

    private var step: String? = null
    private var group: String? = null
    private var selectedProfiles = mutableListOf<String>()
    private var selectedOptionButton: RadioButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        step = arguments?.getString("STEP")
        group = arguments?.getString("GROUP")
        selectedProfiles = arguments?.getStringArrayList("SELECTED_PROFILES")?.toMutableList() ?: mutableListOf()

        binding.tvStep.text = step
        binding.tvGroupTitle.text = group

        when (group) {
            "GRUPO 01" -> {
                binding.rbOption1.text = "DIRETO"
                binding.rbOption2.text = "PERSUASIVO"
                binding.rbOption3.text = "COMPREENSIVO"
                binding.rbOption4.text = "CUIDADOSO"
            }
            "GRUPO 02" -> {
                binding.rbOption1.text = "ASSERTIVO"
                binding.rbOption2.text = "OTIMISTA"
                binding.rbOption3.text = "PACIENTE"
                binding.rbOption4.text = "LOGICO"
            }
            "GRUPO 03" -> {
                binding.rbOption1.text = "EXECUTOR"
                binding.rbOption2.text = "INSPIRADOR"
                binding.rbOption3.text = "PERSISTENTE"
                binding.rbOption4.text = "ORGANIZADO"
            }
            "GRUPO 04" -> {
                binding.rbOption1.text = "DECIDIDO"
                binding.rbOption2.text = "FLEXIVEL"
                binding.rbOption3.text = "ESTAVEL"
                binding.rbOption4.text = "EXATO"
            }
            "GRUPO 05" -> {
                binding.rbOption1.text = "FORMAL"
                binding.rbOption2.text = "EXPRESSIVO"
                binding.rbOption3.text = "AMAVEL"
                binding.rbOption4.text = "FIRME"
            }
        }

        binding.rbOption1.setOnClickListener { onOptionSelected(binding.rbOption1) }
        binding.rbOption2.setOnClickListener { onOptionSelected(binding.rbOption2) }
        binding.rbOption3.setOnClickListener { onOptionSelected(binding.rbOption3) }
        binding.rbOption4.setOnClickListener { onOptionSelected(binding.rbOption4) }

        binding.btnNext.setOnClickListener {
            val selectedProfile = when (selectedOptionButton) {
                binding.rbOption1 -> "D"
                binding.rbOption2 -> "I"
                binding.rbOption3 -> "S"
                binding.rbOption4 -> "C"
                else -> ""
            }

            if (selectedProfile.isNotEmpty()) {
                selectedProfiles.add(selectedProfile)

                if (group == "GRUPO 05") {
                    val instructionFragment2 = InstructionFragment2.newInstance()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, instructionFragment2)
                        .commit()
                } else {
                    val nextGroup = getNextGroup(group)
                    val nextFragment = GroupFragment.newInstance(step ?: "", nextGroup, selectedProfiles)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, nextFragment)
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        updateProgressBar(group)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onOptionSelected(selectedOption: RadioButton) {
        if (selectedOption == selectedOptionButton) {
            selectedOption.isChecked = false
            selectedOptionButton = null
        } else {
            selectedOptionButton?.isChecked = false
            selectedOption.isChecked = true
            selectedOptionButton = selectedOption
        }
    }

    private fun getNextGroup(currentGroup: String?): String {
        return when (currentGroup) {
            "GRUPO 01" -> "GRUPO 02"
            "GRUPO 02" -> "GRUPO 03"
            "GRUPO 03" -> "GRUPO 04"
            "GRUPO 04" -> "GRUPO 05"
            else -> "GRUPO 01"
        }
    }

    companion object {
        fun newInstance(step: String, group: String, selectedProfiles: List<String>): GroupFragment {
            val fragment = GroupFragment()
            val args = Bundle()
            args.putString("STEP", step)
            args.putString("GROUP", group)
            args.putStringArrayList("SELECTED_PROFILES", ArrayList(selectedProfiles))
            fragment.arguments = args
            return fragment
        }
    }

    private fun updateProgressBar(currentGroup: String?) {
        binding.progressCircle1.setImageResource(R.drawable.progress_circle_inactive)
        binding.progressCircle2.setImageResource(R.drawable.progress_circle_inactive)
        binding.progressCircle3.setImageResource(R.drawable.progress_circle_inactive)
        binding.progressCircle4.setImageResource(R.drawable.progress_circle_inactive)
        binding.progressCircle5.setImageResource(R.drawable.progress_circle_inactive)

        when (currentGroup) {
            "GRUPO 01" -> binding.progressCircle1.setImageResource(R.drawable.progress_circle_active)
            "GRUPO 02" -> binding.progressCircle2.setImageResource(R.drawable.progress_circle_active)
            "GRUPO 03" -> binding.progressCircle3.setImageResource(R.drawable.progress_circle_active)
            "GRUPO 04" -> binding.progressCircle4.setImageResource(R.drawable.progress_circle_active)
            "GRUPO 05" -> binding.progressCircle5.setImageResource(R.drawable.progress_circle_active)
        }
    }
}