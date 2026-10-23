package id.gritlabs.dzikirku.ui.onboarding.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.gritlabs.dzikirku.data.Prefs
import id.gritlabs.dzikirku.databinding.FragmentOnBoardLanguageBinding
import id.gritlabs.dzikirku.model.events.OnBoardEvent
import id.gritlabs.dzikirku.model.events.OnBoardType
import id.gritlabs.dzikirku.util.RxBus

class OnBoardLanguageFragment:Fragment(){

    lateinit var binding: FragmentOnBoardLanguageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  FragmentOnBoardLanguageBinding.inflate(LayoutInflater.from(requireActivity()), container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.english.setOnClickListener {
            Prefs.language = "english"
            RxBus.getDefault().send(OnBoardEvent(OnBoardType.LOCATION))
        }
        binding.bahasa.setOnClickListener {
            Prefs.language = "bahasa"
            RxBus.getDefault().send(OnBoardEvent(OnBoardType.LOCATION))
        }
    }
    
}

