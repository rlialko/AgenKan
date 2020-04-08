package com.agenkan.app.presentation.ui.registration

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity.RESULT_OK
import android.app.admin.DevicePolicyManager
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.agenkan.app.R
import com.agenkan.app.ext.showToast
import com.agenkan.app.ext.visible
import com.agenkan.app.presentation.service.ProtectService
import com.agenkan.app.presentation.util.PolicyManager
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.fragment_registration.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class RegistrationFragment : Fragment(R.layout.fragment_registration) {

    private val vm: RegistrationViewModel by viewModel()
    private val policyManager: PolicyManager by inject()
    private lateinit var navController: NavController
    private lateinit var rxPermissions: RxPermissions
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var manager: LocationManager
    private lateinit var settingsClient: SettingsClient

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        navController = findNavController()
        rxPermissions = RxPermissions(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        manager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        settingsClient = LocationServices.getSettingsClient(activity!!)
        buttonStart.setOnClickListener {
            checkAllPermissionsAndRegister()
        }
        observeViewModel()
        vm.init()
    }

    private fun observeViewModel() {
        vm.authenticationState.observe(viewLifecycleOwner, Observer {
            when (it) {
                RegistrationViewModel.AuthenticationState.PENDING_AUTHENTICATION -> {
                    progressBar.visible(true)
                }
                RegistrationViewModel.AuthenticationState.INVALID_AUTHENTICATION -> {
                    progressBar.visible(false)
                    showAuthErrorMessage()
                }
                RegistrationViewModel.AuthenticationState.AUTHENTICATED -> {
                    progressBar.visible(false)
                    navController.navigate(RegistrationFragmentDirections.actionRegistrationFragmentToProfileFragment())
                }
                RegistrationViewModel.AuthenticationState.AUTHENTICATED_BLOCKED -> {
                    progressBar.visible(false)
                    navController.navigate(RegistrationFragmentDirections.actionRegistrationFragmentToProfileFragment())
                    ProtectService.start(context!!)
                }
                else -> {
                    progressBar.visible(false)
                }
            }
        })
    }

    private fun showAuthErrorMessage() {
        showToast(R.string.registration_failed_error)
    }

    private fun showPermissionNotGrantedError() {
        showToast(R.string.registration_permission_not_granted_error)
    }

    @SuppressLint("CheckResult")
    private fun checkAllPermissionsAndRegister() {
        if (!checkAdminPermission()) {
            askAdminPermission()
            return
        }
        if (!checkDrawPermission()) {
            askOverlayDrawPermission()
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            rxPermissions.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
            ).subscribe { granted ->
                checkPhoneStateAndRegister(granted)

            }
        } else {
            rxPermissions.request(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE
            ).subscribe { granted ->
                checkPhoneStateAndRegister(granted)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun checkPhoneStateAndRegister(granted: Boolean) {
        if (granted) {
            checkLocationSettingsRegister()
        } else {
            showPermissionNotGrantedError()
        }
    }

    private fun checkLocationSettingsRegister() {

        val locationSettingsRequest = LocationSettingsRequest.Builder()
            .addLocationRequest(
                LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            )
            .setAlwaysShow(true).build()

        settingsClient.checkLocationSettings(locationSettingsRequest)
            .addOnSuccessListener {
                getLocationAndRegister()
            }.addOnFailureListener { exception ->
                if (exception is ResolvableApiException) {
                    try {
                        startIntentSenderForResult(
                            exception.resolution.intentSender,
                            LOCATION_SETTINGS_REQUEST_CODE, null, 0, 0, 0, null
                        )
                    } catch (sendEx: IntentSender.SendIntentException) {
                        showPermissionNotGrantedError()
                    }
                }
            }
    }

    private fun getLocationAndRegister() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                location?.let {
                    vm.register(location)
                }
            }
    }

    private fun checkAdminPermission() = policyManager.isAdminActive

    private fun checkDrawPermission() =
        Build.VERSION.SDK_INT < 23 || Settings.canDrawOverlays(activity)

    private fun askAdminPermission() {
        val activateDeviceAdmin = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN)
        activateDeviceAdmin.apply {
            putExtra(
                DevicePolicyManager.EXTRA_DEVICE_ADMIN,
                policyManager.devAdminReceiver
            )
            putExtra(
                DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                getString(R.string.sample_device_admin_description)
            )
        }
        startActivityForResult(
            activateDeviceAdmin,
            PM_ACTIVATION_REQUEST_CODE
        )
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun askOverlayDrawPermission() {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${activity?.packageName}")
        )
        startActivityForResult(
            intent,
            OVERLAY_DRAW_REQUEST_CODE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PM_ACTIVATION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                checkAllPermissionsAndRegister()
            }
        }
        if (requestCode == OVERLAY_DRAW_REQUEST_CODE) {
            if (checkDrawPermission()) {
                checkAllPermissionsAndRegister()
            }
        }
        if (requestCode == LOCATION_SETTINGS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                getLocationAndRegister()
            }
        }
    }

    companion object {
        private const val LOCATION_SETTINGS_REQUEST_CODE = 300
        private const val OVERLAY_DRAW_REQUEST_CODE = 200
        private const val PM_ACTIVATION_REQUEST_CODE = 100

    }

}