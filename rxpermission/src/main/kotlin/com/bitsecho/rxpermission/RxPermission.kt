package com.bitsecho.rxpermission

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import io.reactivex.rxjava3.core.Observable
import com.bitsecho.util.RxBus
import com.bitsecho.rxpermission.RxPermissionFragment.Companion.TAG
import io.reactivex.rxjava3.disposables.Disposable

fun FragmentActivity.requestRxPermission(vararg permissions: String): Observable<PermissionResult> = RxPermission(this).request(*permissions)
fun FragmentActivity.requestPermissionOnce(vararg permissions: String, subscribe: (result: PermissionResult)->Unit) {
    var disposable: Disposable? = null
    disposable = requestRxPermission(*permissions).subscribe {
        subscribe(it)
        disposable?.dispose()
    }
}

class RxPermission(private val activity: FragmentActivity) {
    fun request(vararg permissions: String): Observable<PermissionResult> {
        val rxPermissionFragment = getRxPermissionFragment()
        val permissionsResultObs = rxPermissionFragment.permissionObs
        rxPermissionFragment.requestRxPermissions(permissions)
        return permissionsResultObs
    }

    private fun getRxPermissionFragment(): RxPermissionFragment {
        val fragmentManager = activity.supportFragmentManager
        val rxPermissionFragment = fragmentManager.findFragmentByTag(TAG)
        return if(rxPermissionFragment == null) {
            val rxPermissionsFragment = RxPermissionFragment()
            fragmentManager.beginTransaction()
                .add(rxPermissionsFragment, TAG)
                .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
            rxPermissionsFragment
        } else {
            rxPermissionFragment as RxPermissionFragment
        }
    }
}

data class PermissionResult(val isGrant: Boolean, val shouldShowRequestPermissionRationale: Boolean)

class RxPermissionFragment: Fragment() {
    companion object {
        const val PERMISSIONS_REQUEST_CODE = 1125
        const val TAG = "RxPermissionFragment"
    }

    private val permissionsBus = RxBus<PermissionResult>()
    val permissionObs: Observable<PermissionResult> get() = permissionsBus.obs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    fun requestRxPermissions(permissions: Array<out String>) = requestPermissions(permissions, PERMISSIONS_REQUEST_CODE)

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != PERMISSIONS_REQUEST_CODE) return
        val shouldShowRequestPermissionRationaleResult = permissions.map { shouldShowRequestPermissionRationale(it) }
            .reduce { acc: Boolean, b: Boolean -> acc or b }
        val grantResult = grantResults.map { it == PackageManager.PERMISSION_GRANTED }
            .reduce { acc: Boolean, b: Boolean -> acc and b }
        permissionsBus.post(PermissionResult(grantResult, shouldShowRequestPermissionRationaleResult))
    }
}