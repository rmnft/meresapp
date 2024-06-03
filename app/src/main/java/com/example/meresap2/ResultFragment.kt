package com.example.meresap2

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.meresap2.databinding.FragmentResultBinding
import java.io.File
import java.io.FileOutputStream
import android.os.Environment
import android.media.MediaScannerConnection
import androidx.core.content.FileProvider
import android.net.Uri

class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val selectedProfiles = arguments?.getStringArrayList("SELECTED_PROFILES")

        if (selectedProfiles != null) {
            val result = calculateResult(selectedProfiles)
            binding.tvResult.text = getProfileNameAndAcronym(result)

            val profileImage = when {
                result.contains("D") -> R.drawable.perfil_dominante
                result.contains("I") -> R.drawable.perfil_influente
                result.contains("S") -> R.drawable.perfil_estavel
                result.contains("C") -> R.drawable.perfil_conforme
                else -> R.drawable.default_profile_image // Imagem padrão caso o resultado não corresponda a nenhum perfil
            }
            binding.ivProfileImage.setImageResource(profileImage)
        }
        binding.btnSaveAsImage.setOnClickListener {
            captureScreenAsImage(binding.root)
        }

        binding.btnSendEmail.setOnClickListener {
            sendResultByEmail()
        }

        binding.btnWebsite.setOnClickListener {
            openWebsite()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun calculateResult(selectedProfiles: List<String>): String {
        val profileCounts = mutableMapOf(
            "D" to 0,
            "I" to 0,
            "S" to 0,
            "C" to 0
        )

        selectedProfiles.forEach { profile ->
            profileCounts[profile] = profileCounts[profile]?.plus(1) ?: 1
        }

        val maxCount = profileCounts.values.maxOrNull() ?: 0
        val dominantProfiles = profileCounts.filterValues { it == maxCount }.keys.toList()

        return when {
            dominantProfiles.size == 1 -> dominantProfiles[0]
            dominantProfiles.size == 2 -> combineTwoProfiles(dominantProfiles[0], dominantProfiles[1])
            else -> ""
        }
    }

    private fun combineTwoProfiles(profile1: String, profile2: String): String {
        return "$profile1$profile2"
    }

    private fun getProfileNameAndAcronym(profile: String): String {
        return when (profile) {
            "D" -> "DOMINANTE(D)"
            "I" -> "INFLUENTE(I)"
            "S" -> "ESTÁVEL(S)"
            "C" -> "CONFORME(C)"
            else -> ""
        }
    }

    companion object {
        fun newInstance(selectedProfiles: List<String>): ResultFragment {
            val fragment = ResultFragment()
            val args = Bundle()
            args.putStringArrayList("SELECTED_PROFILES", ArrayList(selectedProfiles))
            fragment.arguments = args
            return fragment
        }
    }

    private fun captureScreenAsImage(view: View) {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)

        val fileName = "resultado_${System.currentTimeMillis()}.png"
        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        MediaScannerConnection.scanFile(requireContext(), arrayOf(file.absolutePath), null, null)

        Toast.makeText(requireContext(), "Imagem salva na galeria", Toast.LENGTH_SHORT).show()
    }

    private fun sendResultByEmail() {
        val bitmap = captureScreenAsBitmap(binding.root)

        val fileName = "resultado_${System.currentTimeMillis()}.png"
        val file = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), fileName)
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

        val uri = FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", file)

        val emailIntent = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_SUBJECT, "Resultado da Análise de Perfil Comportamental")
            putExtra(Intent.EXTRA_TEXT, "Segue em anexo o resultado da análise de perfil comportamental.")
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        startActivity(Intent.createChooser(emailIntent, "Enviar resultado por e-mail"))
    }

    private fun captureScreenAsBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun openWebsite() {
        val websiteUrl = "https://blog.solides.com.br/analisar-perfil-disc/"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(websiteUrl))
        startActivity(intent)
    }
}