package app.sinanyilmazdev.crazyshoppingcart

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import app.sinanyilmazdev.crazyshoppingcart.util.GameUtils
import java.lang.ref.WeakReference
import android.view.View
import app.sinanyilmazdev.crazyshoppingcart.camera.LensEnginePreview
import app.sinanyilmazdev.crazyshoppingcart.databinding.ActivityMainBinding
import app.sinanyilmazdev.crazyshoppingcart.util.viewBinding


class MainActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityMainBinding::inflate)

    private var timeStr: String? = null
    private var isPermission = false

    private var magnification = 1f
    private var timeSecond = 60
    private var speed = 6

    private var lensEnginePreview : LensEnginePreview? = null

    private val handler = TimeHandler(this@MainActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (!isGranted(Manifest.permission.CAMERA)) {
            requestPermission(PERMISSIONS, REQUEST_CODE)
        }

        init()
        initListener()
    }

    private fun init() {
        lensEnginePreview = binding.preview

        timeStr = getString(R.string.time)

        binding.apply {
            time.text = timeStr + timeSecond

            GameUtils().apply {
                createHandAnalyze()
                magnification = GameUtils().getMagnification()
                initLensEngine(this@MainActivity, lensEnginePreview!!)

                gameGraphic.initData(this@MainActivity, binding.topScore, binding.score, binding.gameOver,magnification)
                setHandTransactor(gameGraphic)
            }
        }
    }

    private fun initListener() {
        binding.apply {
            start.setOnClickListener(onClickListener)
            back.setOnClickListener(onClickListener)
            restart.setOnClickListener(onClickListener)
        }
    }

    private val onClickListener: View.OnClickListener = View.OnClickListener { v ->
        when (v.id) {
            R.id.start -> {
                binding.startGame.visibility = View.GONE
                binding.gameGraphic.apply {
                    setSpeed(4)
                    startGame()
                    invalidate()
                }
                handler.sendEmptyMessage(START_GAME)
            }
            R.id.back -> finish()
            R.id.restart -> {
                timeSecond = 60

                binding.apply {
                    time.text = timeStr + time
                    gameOver.visibility = View.GONE
                    gameGraphic.startGame()
                    gameGraphic.invalidate()
                }
                handler.sendEmptyMessage(START_GAME)
            }
            else -> {}
        }
    }

    override fun onPause() {
        super.onPause()
        lensEnginePreview?.let { GameUtils().stopPreview(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        GameUtils().releaseAnalyze()
        handler.removeCallbacksAndMessages(null)
    }

    private fun isGranted(permission: String): Boolean {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            isPermission = true
            true
        } else {
            val checkSelfPermission = checkSelfPermission(permission)
            isPermission = checkSelfPermission == PackageManager.PERMISSION_GRANTED
            isPermission
        }
    }

    private fun requestPermission(permissions: Array<String>, requestCode: Int): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        if (!isGranted(permissions[0])) {
            requestPermissions(permissions, requestCode)
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (!isGranted(Manifest.permission.CAMERA)) {
                requestPermission(PERMISSIONS, REQUEST_CODE)
            } else {
                magnification = GameUtils().getMagnification()
            }
        }
    }

    internal class TimeHandler(mainActivity: MainActivity) : Handler(Looper.getMainLooper()) {
        private val weakReference: WeakReference<MainActivity>?

        init {
            weakReference = WeakReference(mainActivity)
        }

        @SuppressLint("SetTextI18n")
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)

            if (weakReference?.get() == null) {
                return
            }
            val mainActivity: MainActivity = weakReference.get()!!

            when (msg.what) {
                START_GAME -> {
                    mainActivity.handler.sendEmptyMessageDelayed(CHECK_TIME, 1000)
                    mainActivity.binding.time.text = mainActivity.timeStr + mainActivity.timeSecond
                }
                CHECK_TIME -> {
                    mainActivity.timeSecond--
                    when (mainActivity.timeSecond) {
                        45 -> {
                            mainActivity.speed = 8
                            mainActivity.binding.gameGraphic.setSpeed(mainActivity.speed)
                        }
                        30 -> {
                            mainActivity.speed = 10
                            mainActivity.binding.gameGraphic.setSpeed(mainActivity.speed)
                        }
                        15 -> {
                            mainActivity.speed = 12
                            mainActivity.binding.gameGraphic.setSpeed(mainActivity.speed)
                        }
                    }
                    mainActivity.binding.time.text = mainActivity.timeStr + mainActivity.timeSecond
                    if (mainActivity.timeSecond <= 0) {
                        mainActivity.binding.gameGraphic.gameOver()
                    } else {
                        mainActivity.handler.sendEmptyMessageDelayed(CHECK_TIME, 1000)
                    }
                }
                else -> {}
            }
        }
    }

    companion object {
        private val PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        private const val REQUEST_CODE = 1
        private const val START_GAME = 2
        private const val CHECK_TIME = 3
    }
}