package com.agenkan.app.presentation.ui.profile

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.agenkan.app.R
import com.agenkan.app.data.ContentResult
import com.agenkan.app.ext.visible
import com.agenkan.app.presentation.util.ImeiFormatter
import com.agenkan.app.presentation.util.LocationFormatter
import kotlinx.android.synthetic.main.fragment_profile.*
import net.glxn.qrgen.android.QRCode
import org.koin.android.ext.android.inject


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val vm: ProfileViewModel by inject()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        buttonDeactivate.setOnClickListener { vm.logout() }
        observeViewModel()
    }

    private fun observeViewModel() {
        vm.toast.observe(viewLifecycleOwner, Observer {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
        vm.appState.observe(viewLifecycleOwner, Observer {
            when (it) {
                ProfileViewModel.AppState.UN_AUTHENTICATED -> {
                    findNavController().navigate(
                        ProfileFragmentDirections.actionProfileFragmentToRegistrationFragment()
                    )
                }
                ProfileViewModel.AppState.AUTHENTICATED -> {

                }
                ProfileViewModel.AppState.USER_CONNECTED -> {

                }
            }
        })

        vm.device.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Error -> Log.d("Repository", "no data")
                is ContentResult.Success -> {
                    it.data.let { device ->
                        textImeiValue.text = ImeiFormatter.format(device.imei)
                        textLocationValue.text =
                            LocationFormatter.format(device.longitude, device.latitude)
                        if (device.isConnected) {
                            textTitle.text = getString(R.string.profile_connected_title)
                            textSubtitle.text = getString(R.string.profile_connected_subtitle)

                            layoutQr.visible(false)

                            textUserId.visible(true)
                            textUserIdValue.visible(true)
                            textUserIdValue.text = device.agentId

                            textCode.visible(true)

                            textCodeValue.visible(true)
                            textCodeValue.text = String.format("%d", device.installId)

                            buttonDeactivate.visible(false)
                        } else {
                            textTitle.text = getString(R.string.profile_title)
                            textSubtitle.text = getString(R.string.profile_subtitle)

                            layoutQr.visible(true)
                            val qrValue = String.format("%d", device.installId)
                            textCodeQr.text = qrValue
                            setQr(qrValue, imageViewQr)

                            textUserId.visible(false)
                            textUserIdValue.visible(false)
                            textCode.visible(false)
                            textCodeValue.visible(false)

                            buttonDeactivate.visible(true)
                        }
                    }
                }
            }
        })
        vm.getProfile()
    }

    private fun setQr(value: String, imageViewQr: ImageView) {
        try {
            imageViewQr.setImageBitmap(QRCode.from(value).bitmap())
        } catch (e: Exception) {
            imageViewQr.setImageBitmap(null)
        }
    }

}
