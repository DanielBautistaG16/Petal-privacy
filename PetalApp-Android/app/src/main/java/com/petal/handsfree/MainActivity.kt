package com.petal.handsfree

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.petal.handsfree.databinding.ActivityMainBinding
import com.petal.handsfree.service.VoiceService
import com.petal.handsfree.utils.AlertCopsHandler
import com.petal.handsfree.viewmodel.MainViewModel
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    private var currentAnimator: AnimatorSet? = null

    private val requiredPermissions = arrayOf(
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.READ_CONTACTS,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.ANSWER_PHONE_CALLS
    )

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allGranted = permissions.values.all { it }
        if (allGranted) {
            onAllPermissionsGranted()
        } else {
            handlePermissionsDenied(permissions)
        }
    }

    private val batteryOptimizationLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { _ ->
        checkBatteryOptimization()
    }

    override fun attachBaseContext(newBase: Context) {
        val locale = Locale("es")
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViewModel()
        setupUI()
        checkPermissionsAndStart()
        observeVoiceState()
    }

    private fun initializeViewModel() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        viewModel.serviceStatus.observe(this) { isRunning ->
            // Service is always on, just update status dot
            updateStatusDot(isRunning)
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                showError(it)
                viewModel.clearError()
            }
        }
    }

    private fun observeVoiceState() {
        VoiceService.currentState.observe(this) { state ->
            when (state) {
                VoiceService.VoiceState.LISTENING -> showListeningState()
                VoiceService.VoiceState.WAKE_WORD_DETECTED -> showWakeWordDetectedState()
                VoiceService.VoiceState.PROCESSING -> showProcessingState()
                null -> showListeningState()
            }
        }
    }

    private fun showListeningState() {
        currentAnimator?.cancel()
        binding.viewStateIndicator.setBackgroundResource(R.drawable.indicator_listening)
        binding.viewStateIndicator.scaleX = 1f
        binding.viewStateIndicator.scaleY = 1f
        binding.tvMicLabel.text = getString(R.string.state_listening)
        binding.tvMicLabel.setTextColor(ContextCompat.getColor(this, R.color.text_secondary))
        binding.ivMicIcon.setColorFilter(
            ContextCompat.getColor(this, R.color.text_secondary)
        )

        val alphaAnim = ObjectAnimator.ofFloat(binding.viewStateIndicator, "alpha", 0.5f, 1.0f).apply {
            duration = 1500
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }
        currentAnimator = AnimatorSet().apply {
            play(alphaAnim)
            start()
        }
    }

    private fun showWakeWordDetectedState() {
        currentAnimator?.cancel()
        binding.viewStateIndicator.setBackgroundResource(R.drawable.indicator_active)
        binding.viewStateIndicator.scaleX = 1f
        binding.viewStateIndicator.scaleY = 1f
        binding.tvMicLabel.text = getString(R.string.state_wake_word)
        binding.tvMicLabel.setTextColor(ContextCompat.getColor(this, R.color.text_on_primary))
        binding.ivMicIcon.setColorFilter(
            ContextCompat.getColor(this, R.color.text_on_primary)
        )

        val scaleX = ObjectAnimator.ofFloat(binding.viewStateIndicator, "scaleX", 1f, 1.12f).apply {
            duration = 400
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }
        val scaleY = ObjectAnimator.ofFloat(binding.viewStateIndicator, "scaleY", 1f, 1.12f).apply {
            duration = 400
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }
        currentAnimator = AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            start()
        }
    }

    private fun showProcessingState() {
        currentAnimator?.cancel()
        binding.viewStateIndicator.alpha = 1f
        binding.viewStateIndicator.setBackgroundResource(R.drawable.indicator_processing)
        binding.tvMicLabel.text = getString(R.string.state_processing)
        binding.ivMicIcon.setColorFilter(
            ContextCompat.getColor(this, R.color.text_on_primary)
        )

        val scaleX = ObjectAnimator.ofFloat(binding.viewStateIndicator, "scaleX", 1f, 1.08f).apply {
            duration = 600
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }
        val scaleY = ObjectAnimator.ofFloat(binding.viewStateIndicator, "scaleY", 1f, 1.08f).apply {
            duration = 600
            repeatMode = ObjectAnimator.REVERSE
            repeatCount = ObjectAnimator.INFINITE
        }
        currentAnimator = AnimatorSet().apply {
            playTogether(scaleX, scaleY)
            start()
        }
    }

    private fun setupUI() {
        binding.apply {
            ivPetalLogo.setOnClickListener { openPetalWebsite() }

            // Indicator is not clickable - just visual
            viewStateIndicator.isClickable = false

            btnStolenBike.setOnClickListener { openAlertCops() }
            btnLocatePetal.setOnClickListener { openFindMyDevice() }

            // Hidden buttons
            btnSettings.setOnClickListener { openAppSettings() }
            btnBatteryOptimization.setOnClickListener { requestBatteryOptimizationExemption() }
            btnHelp.setOnClickListener { showHelpDialog() }
            btnSendCommand.setOnClickListener { sendTextCommand() }
            etTextCommand.setOnEditorActionListener { _, _, _ ->
                sendTextCommand()
                true
            }
            btnLanguageToggle.setOnClickListener { toggleLanguage() }
            btnTestCall.setOnClickListener { testCall() }
            btnTestNavigation.setOnClickListener { testNavigation() }
        }

        showListeningState()
    }

    private fun updateStatusDot(isRunning: Boolean) {
        binding.statusDot.setBackgroundResource(
            if (isRunning) R.drawable.status_dot_active else R.drawable.status_dot_inactive
        )
        binding.tvServiceStatus.text = getString(
            if (isRunning) R.string.service_active else R.string.service_inactive
        )
        binding.tvServiceBody.text = getString(
            if (isRunning) R.string.service_active_body else R.string.service_inactive_body
        )
    }

    private fun toggleLanguage() {
        val prefs = getSharedPreferences("petal_prefs", Context.MODE_PRIVATE)
        val currentLang = prefs.getString("app_language", "es") ?: "es"
        val newLang = if (currentLang == "es") "en" else "es"
        prefs.edit().putString("app_language", newLang).apply()
        recreate()
    }

    private fun checkPermissionsAndStart() {
        if (checkAllPermissions()) {
            checkBatteryOptimization()
            if (!VoiceService.isRunning) {
                startVoiceService()
            }
        } else {
            requestPermissions()
        }
    }

    private fun checkAllPermissions(): Boolean {
        return requiredPermissions.all { permission ->
            ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        permissionLauncher.launch(requiredPermissions)
    }

    private fun onAllPermissionsGranted() {
        checkBatteryOptimization()
        startVoiceService()
    }

    private fun handlePermissionsDenied(permissions: Map<String, Boolean>) {
        val deniedPermissions = permissions.filter { !it.value }.keys
        val criticalDenied = deniedPermissions.intersect(
            setOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CALL_PHONE)
        )
        if (criticalDenied.isNotEmpty()) {
            showError(getString(R.string.error_critical_permissions))
        } else {
            val audioGranted = ContextCompat.checkSelfPermission(
                this, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
            if (audioGranted) startVoiceService()
            else showError(getString(R.string.mic_permission_required))
        }
    }

    private fun startVoiceService() {
        val intent = Intent(this, VoiceService::class.java).apply {
            action = VoiceService.ACTION_START_SERVICE
        }
        startForegroundService(intent)
        viewModel.updateServiceStatus(true)
    }

    private fun sendTextCommand() {
        val command = binding.etTextCommand.text.toString().trim()
        if (command.isEmpty()) {
            Toast.makeText(this, getString(R.string.toast_write_command_empty), Toast.LENGTH_SHORT).show()
            return
        }
        if (!VoiceService.isRunning) {
            Toast.makeText(this, getString(R.string.toast_service_not_running), Toast.LENGTH_SHORT).show()
            return
        }
        val intent = Intent(this, VoiceService::class.java).apply {
            action = VoiceService.ACTION_PROCESS_TEXT_COMMAND
            putExtra(VoiceService.EXTRA_TEXT_COMMAND, command)
        }
        startService(intent)
        binding.etTextCommand.text.clear()
        Toast.makeText(this, getString(R.string.toast_processing, command), Toast.LENGTH_SHORT).show()
    }

    private fun checkBatteryOptimization() {
        val powerManager = getSystemService(POWER_SERVICE) as android.os.PowerManager
        if (!powerManager.isIgnoringBatteryOptimizations(packageName)) {
            binding.tvBatteryWarning.visibility = android.view.View.VISIBLE
            binding.btnBatteryOptimization.visibility = android.view.View.VISIBLE
        } else {
            binding.tvBatteryWarning.visibility = android.view.View.GONE
            binding.btnBatteryOptimization.visibility = android.view.View.GONE
        }
    }

    private fun requestBatteryOptimizationExemption() {
        val intent = Intent().apply {
            action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            data = Uri.parse("package:$packageName")
        }
        batteryOptimizationLauncher.launch(intent)
    }

    private fun openAppSettings() {
        val intent = Intent().apply {
            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.parse("package:$packageName")
        }
        startActivity(intent)
    }

    private fun showHelpDialog() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.help_title))
            .setMessage(getString(R.string.help_commands_body) + "\n\n" + getString(R.string.help_tips_body))
            .setPositiveButton(getString(R.string.help_understood), null)
            .show()
    }

    private fun testCall() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Funcionalidad de llamada: OK", Toast.LENGTH_SHORT).show()
        } else {
            showError(getString(R.string.call_permission_denied))
        }
    }

    private fun testNavigation() {
        val testIntent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("google.navigation:q=Valencia,Spain&mode=b")
        }
        if (testIntent.resolveActivity(packageManager) != null) {
            Toast.makeText(this, "Funcionalidad de navegación: OK", Toast.LENGTH_SHORT).show()
        } else {
            showError(getString(R.string.navigation_app_not_found))
        }
    }

    private fun openPetalWebsite() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://ridepetal.com/es-es")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } catch (e: Exception) {
            showError(getString(R.string.error_network))
        }
    }

    private fun openFindMyDevice() {
        val packages = listOf(
            "com.google.android.apps.adm",
            "com.google.android.gms",
            "com.google.android.apps.findmydevice"
        )
        for (pkg in packages) {
            val launchIntent = packageManager.getLaunchIntentForPackage(pkg)
            if (launchIntent != null && pkg != "com.google.android.gms") {
                startActivity(launchIntent)
                return
            }
        }
        try {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://www.google.com/android/find")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            })
        } catch (e: Exception) {
            Toast.makeText(this, "No se pudo abrir el Localizador de Google", Toast.LENGTH_LONG).show()
        }
    }

    private fun openAlertCops() {
        val alertCopsHandler = AlertCopsHandler(this)
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.theft_alert_title))
            .setMessage(getString(R.string.theft_alert_body) + "\n\n" + alertCopsHandler.getStatusMessage())
            .setPositiveButton(getString(R.string.open_alertcops)) { _, _ ->
                val success = alertCopsHandler.openAlertCops()
                if (success) {
                    Toast.makeText(this, getString(R.string.toast_opening_alertcops), Toast.LENGTH_LONG).show()
                } else {
                    showError(getString(R.string.error_network))
                }
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show()
    }

    private fun showError(message: String) {
        Toast.makeText(this, "Error: $message", Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateServiceStatus(VoiceService.isRunning)
        checkBatteryOptimization()
    }

    override fun onDestroy() {
        currentAnimator?.cancel()
        super.onDestroy()
    }
}
