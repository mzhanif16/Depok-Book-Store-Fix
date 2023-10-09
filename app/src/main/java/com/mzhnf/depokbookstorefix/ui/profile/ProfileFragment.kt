package com.mzhnf.depokbookstorefix.ui.profile

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.mzhnf.depokbookstorefix.DepokBookStore
import com.mzhnf.depokbookstorefix.R
import com.mzhnf.depokbookstorefix.activity.LoginActivity
import com.mzhnf.depokbookstorefix.databinding.FragmentHomeBinding
import com.mzhnf.depokbookstorefix.databinding.FragmentProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }


    }
    private fun showLogoutConfirmationDialog() {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.setIcon(R.drawable.logo)
        alertDialogBuilder.setTitle("Konfirmasi Logout")
        alertDialogBuilder.setMessage("Apakah Anda yakin ingin logout?")
        alertDialogBuilder.setPositiveButton("Ya") { _, _ ->
            // Logout jika pengguna menekan tombol "Ya"
            logout()
        }
        alertDialogBuilder.setNegativeButton("Tidak") { dialog, _ ->
            // Batalkan dialog jika pengguna menekan tombol "Tidak"
            dialog.dismiss()
        }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun logout() {
        val depokBookStore = activity?.application as DepokBookStore

        // Hapus token atau data autentikasi
        depokBookStore.removeToken()

        // Arahkan pengguna ke halaman login
        val intent = Intent(activity, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        activity?.finish()
    }
}