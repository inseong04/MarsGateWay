package com.example.marsgateway.view.marsroverlanding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.marsgateway.R
import com.example.marsgateway.adapter.MarsRoverAdapter
import com.example.marsgateway.databinding.FragmentMarsRoverLandingBinding
import com.example.marsgateway.repository.MarsRoverLandingRepository
import com.example.marsgateway.viewmodel.MarsRoverLandingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.text.Editable
import android.text.TextUtils
import android.widget.Toast
import com.example.marsgateway.data.api.NasaServiceImpl

class MarsRoverLandingFragment : Fragment() {

    private lateinit var binding: FragmentMarsRoverLandingBinding
    private val viewModel: MarsRoverLandingViewModel by lazy {
        ViewModelProvider(requireActivity(),
            MarsRoverLandingViewModelFactory(MarsRoverLandingRepository(MyApplication.getApplication())))
            .get(MarsRoverLandingViewModel::class.java)
    }
    private lateinit var adapter: MarsRoverAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_mars_rover_landing,
            container,
            false)
        binding.lifecycleOwner = requireActivity()

        adapter = MarsRoverAdapter(viewModel)
        binding.landingRecyclerView.adapter = adapter

        val watcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (!TextUtils.isEmpty(charSequence.toString())) {
                    val etvLen = binding.landingEtv.text.length
                    if (etvLen == 4 || etvLen == 7) {
                        val text: String = binding.landingEtv.text.toString() + "-"
                        binding.landingEtv.setText(text)
                        binding.landingEtv.setSelection(text.length)
                    }
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        }
        binding.landingEtv.addTextChangedListener(watcher)

        binding.landingSearachBtn.setOnClickListener {
            if (binding.landingEtv.text.length < 10) {
                Toast.makeText(requireContext(), "Please enter the exact date.", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    viewModel.getPhotos(MyApplication.getApplication(),
                        binding.landingEtv.text.toString(),
                        2,
                        DataType.apiKey)
                    binding.landingEtv.setText("")
                    adapter.notifyDataSetChanged()
                }
            }
        }


        return binding.root
    }
}