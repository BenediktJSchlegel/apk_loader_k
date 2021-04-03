package com.example.apk_loader_k

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File

class MainActivity : AppCompatActivity() {
    private val REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 1
    private val REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE = 2
    private val REQUEST_PERMISSION_REQUEST_INSTALL_PACKAGES = 3
    private val REQUEST_PERMISSION_REQUEST_DELETE_PACKAGES = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val topButton = findViewById<Button>(R.id.top_button)
        val permissionButton = findViewById<Button>(R.id.permission_button)
        val installButton = findViewById<Button>(R.id.install_button)
        val input = findViewById<TextView>(R.id.text_input)

        installButton.text = "INSTALL APP"

        val packageManager: PackageManager = this.packageManager
        if(isPackageInstalled(input.text.toString(), packageManager)){
            installButton.text = "DELETE APP"
            installButton.setBackgroundColor(233)
        }

        topButton.setOnClickListener { _ ->
            switchToOtherApp();
        }

        permissionButton.setOnClickListener { _ ->
            showAllPermissions();
        }

        installButton.setOnClickListener { _ ->
            installApk();
        }
    }

    private fun isPackageInstalled(packageName: String, packageManager: PackageManager): Boolean {
        return try {
            packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }

    private fun switchToOtherApp(){
        val text = findViewById<TextView>(R.id.text_input)

        val intent = packageManager.getLaunchIntentForPackage(text.text.toString())
        startActivity(intent)
    }

    private fun installApk(){
        val newFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/payload.apk")

        val ur = FileProvider.getUriForFile(this@MainActivity, BuildConfig.APPLICATION_ID + ".provider", newFile)


        val promptInstall = Intent(Intent.ACTION_VIEW).setDataAndType(ur, "application/vnd.android.package-archive")
        promptInstall.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        promptInstall.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        this.startActivity(promptInstall)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_PERMISSION_READ_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "READ EXTERNAL Granted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "READ EXTERNAL Denied!", Toast.LENGTH_SHORT).show()
                }
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "WRITE EXTERNAL Granted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "WRITE EXTERNAL Denied!", Toast.LENGTH_SHORT).show()
                }
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "DELETE Granted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "DELETE Denied!", Toast.LENGTH_SHORT).show()
                }
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "INSTALL Granted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "INSTALL Denied!", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "WRITE EXTERNAL Granted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "WRITE EXTERNAL Denied!", Toast.LENGTH_SHORT).show()
                }
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "DELETE Granted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "DELETE Denied!", Toast.LENGTH_SHORT).show()
                }
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "INSTALL Granted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "INSTALL Denied!", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_PERMISSION_REQUEST_DELETE_PACKAGES -> {
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "DELETE Granted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "DELETE Denied!", Toast.LENGTH_SHORT).show()
                }
                if (grantResults.isNotEmpty()
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this@MainActivity, "INSTALL Granted!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity, "INSTALL Denied!", Toast.LENGTH_SHORT).show()
                }
            }
            REQUEST_PERMISSION_REQUEST_INSTALL_PACKAGES -> if (grantResults.isNotEmpty()
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "INSTALL Granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "INSTALL Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun showExplanation(title: String,
                                message: String,
                                permission: String,
                                permissionRequestCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok) { _, _ -> requestPermission(permission, permissionRequestCode) }
        builder.create().show()
    }

    private fun requestPermission(permissionName: String, permissionRequestCode: Int) {
        ActivityCompat.requestPermissions(this, arrayOf(permissionName), permissionRequestCode)
    }

    private fun showAllPermissions() {
        showExternalStorageRead()
        showExternalStorageWrite()
        showRequestInstall()
        showRequestDelete()
    }

    private fun showRequestDelete() {
        val permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.REQUEST_DELETE_PACKAGES)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.REQUEST_DELETE_PACKAGES)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.REQUEST_DELETE_PACKAGES, REQUEST_PERMISSION_REQUEST_DELETE_PACKAGES)
            } else {
                requestPermission(Manifest.permission.REQUEST_DELETE_PACKAGES, REQUEST_PERMISSION_REQUEST_DELETE_PACKAGES)
            }
        } else {
            Toast.makeText(this@MainActivity, "DELETE (already) Granted!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showRequestInstall() {
        val permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.REQUEST_INSTALL_PACKAGES)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.REQUEST_INSTALL_PACKAGES, REQUEST_PERMISSION_REQUEST_INSTALL_PACKAGES)
            } else {
                requestPermission(Manifest.permission.REQUEST_INSTALL_PACKAGES, REQUEST_PERMISSION_REQUEST_INSTALL_PACKAGES)
            }
        } else {
            Toast.makeText(this@MainActivity, "INSTALL (already) Granted!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showExternalStorageRead() {
        val permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)
            } else {
                requestPermission(Manifest.permission.READ_EXTERNAL_STORAGE, REQUEST_PERMISSION_READ_EXTERNAL_STORAGE)
            }
        } else {
            Toast.makeText(this@MainActivity, "EXTERNAL READ (already) Granted!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showExternalStorageWrite() {
        val permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showExplanation("Permission Needed", "Rationale", Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE)
            } else {
                requestPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, REQUEST_PERMISSION_WRITE_EXTERNAL_STORAGE)
            }
        } else {
            Toast.makeText(this@MainActivity, "EXTERNAL WRITE (already) Granted!", Toast.LENGTH_SHORT).show()
        }
    }
}