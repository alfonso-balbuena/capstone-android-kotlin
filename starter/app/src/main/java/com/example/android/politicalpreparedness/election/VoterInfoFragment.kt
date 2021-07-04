package com.example.android.politicalpreparedness.election

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.example.android.politicalpreparedness.ElectionApp
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentVoterInfoBinding
import com.google.android.material.snackbar.Snackbar

class VoterInfoFragment : Fragment() {

    private  val viewModel : VoterInfoViewModel by viewModels { VoterInfoViewModelFactory(requireActivity().application as ElectionApp) }
    private lateinit var binding : FragmentVoterInfoBinding

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_voter_info,container,false)
        binding.lifecycleOwner = this
        val args : VoterInfoFragmentArgs by navArgs()
        binding.viewModel = viewModel

        viewModel.reset()
        viewModel.validateData(args.argElectionId,args.argDivision)

        viewModel.areDataValid.observe(viewLifecycleOwner, Observer {
            if(it) {
                viewModel.getVoteInfo(args.argElectionId.toLong(),args.argDivision)
            } else {
                binding.root.visibility = View.GONE
                Snackbar.make(binding.root,R.string.error_voter_info,Snackbar.LENGTH_LONG).show()
            }
        })

        val observer = Observer<String?> { url ->
            url?.let{
                displayURL(it)
            }
        }
        viewModel.votingURL.observe(viewLifecycleOwner, observer)
        viewModel.ballotURL.observe(viewLifecycleOwner, observer)
        return binding.root
    }
    private fun displayURL(url : String) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}