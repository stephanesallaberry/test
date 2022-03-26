package com.appstud.template.android.transport.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.appstud.template.android.databinding.AccountFragmentBinding
import com.appstud.template.android.transport.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AccountFragment : Fragment() {
    private lateinit var accountFragmentBinding: AccountFragmentBinding
    private val viewModel by sharedViewModel<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        accountFragmentBinding = AccountFragmentBinding.inflate(inflater, container, false)
        accountFragmentBinding.lifecycleOwner = viewLifecycleOwner
        return accountFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        accountFragmentBinding.accountLogout.setOnClickListener {
            viewModel.logout()
        }
    }
}
