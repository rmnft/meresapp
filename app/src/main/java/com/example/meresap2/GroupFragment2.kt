package com.example.meresap2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import com.example.meresap2.databinding.FragmentGroup2Binding

class GroupFragment2 : Fragment() {

    private var _binding: FragmentGroup2Binding? = null
    private val binding get() = _binding!!

    private var step: String? = null
    private var group: String? = null
    private var selectedProfiles = mutableListOf<String>()
    private var selectedOptionButton: RadioButton? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroup2Binding.inflate(inflater, container, false)
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
            "GRUPO 06" -> {
                binding.rbOption1.text = "CONSTRUIR UM NEGOCIO LUCRATIVO"
                binding.rbOption2.text = "LIDERAR UM TIME VENCEDOR"
                binding.rbOption3.text = "CONTRIBUIR PARA UM HAMBIENTE HARMONICO"
                binding.rbOption4.text = "DESENVOLVER PESQUISAS RELEVANTES"
            }
            "GRUPO 07" -> {
                binding.rbOption1.text = "ALCANCAR MINHA INDEPENDENCIA FINANCEIRA"
                binding.rbOption2.text = "VIVENCIAR A ARTE EM MINHA VIDA"
                binding.rbOption3.text = "AJUDAR O PROXIMO"
                binding.rbOption4.text = "AUMENTAR MEUS CONHECIMENTOS"
            }
            "GRUPO 08" -> {
                binding.rbOption1.text = "SER UM LIDER COM STATUS E PODER"
                binding.rbOption2.text = "SER UM LIDER QUE SERVE"
                binding.rbOption3.text = "SER UM LIDER QUE PREVALECE O BEM ESTAR"
                binding.rbOption4.text = "SER UM LIDER COM INTELECTUALIDADE"
            }
            "GRUPO 09" -> {
                binding.rbOption1.text = "SEGUIR UMA ESTRATEGIA DE SUCESSO"
                binding.rbOption2.text = "EXPANDIR MINHA PRODUTIVIDADE"
                binding.rbOption3.text = "TER UMA VIDA EM EQUILIBRIO"
                binding.rbOption4.text = "ESTAR SEMPRE APRENDENDO ALGO NOVO"
            }
            "GRUPO 10" -> {
                binding.rbOption1.text = "POTENCIALIZAR RECURSOS FINANCEIROS"
                binding.rbOption2.text = "TER O RECONHECIMENTO E STATUS MERECIDO"
                binding.rbOption3.text = "CONTRIBUIR COM INSTITUICOES DE CARIDADE"
                binding.rbOption4.text = "ADQUIRIR NOVOS CONHECIMENTOS"
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

                if (group == "GRUPO 10") {
                    val resultFragment = ResultFragment.newInstance(selectedProfiles)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, resultFragment)
                        .commit()
                } else {
                    val nextGroup = getNextGroup(group)
                    val nextFragment = GroupFragment2.newInstance(step ?: "", nextGroup, selectedProfiles)
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, nextFragment)
                        .addToBackStack(null)
                        .commit()                }
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
            "GRUPO 06" -> "GRUPO 07"
            "GRUPO 07" -> "GRUPO 08"
            "GRUPO 08" -> "GRUPO 09"
            "GRUPO 09" -> "GRUPO 10"
            else -> "GRUPO 06"
        }
    }

    companion object {
        fun newInstance(step: String, group: String, selectedProfiles: List<String>): GroupFragment2 {
            val fragment = GroupFragment2()
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
            "GRUPO 06" -> binding.progressCircle1.setImageResource(R.drawable.progress_circle_active)
            "GRUPO 07" -> binding.progressCircle2.setImageResource(R.drawable.progress_circle_active)
            "GRUPO 08" -> binding.progressCircle3.setImageResource(R.drawable.progress_circle_active)
            "GRUPO 09" -> binding.progressCircle4.setImageResource(R.drawable.progress_circle_active)
            "GRUPO 10" -> binding.progressCircle5.setImageResource(R.drawable.progress_circle_active)
        }
    }
}