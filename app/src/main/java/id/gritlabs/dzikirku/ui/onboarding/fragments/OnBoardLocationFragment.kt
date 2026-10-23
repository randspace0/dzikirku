package id.gritlabs.dzikirku.ui.onboarding.fragments

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import id.gritlabs.dzikirku.databinding.FragmentOnBoardLocationBinding
import id.gritlabs.dzikirku.model.events.OnBoardEvent
import id.gritlabs.dzikirku.model.events.OnBoardType
import id.gritlabs.dzikirku.ui.onboarding.fragments.viewmodels.OnBoardLoadingViewModel
import id.gritlabs.dzikirku.util.RxBus
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardLocationFragment : Fragment() {

    lateinit var binding: FragmentOnBoardLocationBinding

    val viewModel: OnBoardLoadingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOnBoardLocationBinding.inflate(
            LayoutInflater.from(requireActivity()),
            container,
            false
        )
        return binding.root
    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true && permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true){
                RxBus.getDefault().send(OnBoardEvent(OnBoardType.PRAYTIME))
            }
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.grant.setOnClickListener {
            requestPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
        }
        binding.skip.setOnClickListener {
            startActivity(Intent(requireActivity(),OnBoardLoadingActivity::class.java))
            requireActivity().finish()
        }
    }
}

