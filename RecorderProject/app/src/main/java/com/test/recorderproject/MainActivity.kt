package com.test.recorderproject

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.test.recorderproject.databinding.ActivityMainBinding
import java.io.IOException
import java.lang.Exception

class MainActivity : AppCompatActivity(), OnTimerTickListener {
    lateinit var activityMainBinding: ActivityMainBinding
    lateinit var timer: Timer
    var recoder: MediaRecorder? = null
    var player: MediaPlayer? = null
    var fileName: String = ""
    var state: RecodeState = RecodeState.RELEASE

    companion object {
        const val RECORD_PERMISSION_CODE = 100
    }

    enum class RecodeState {
        RELEASE, RECODING, PLAYING
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        fileName = "${externalCacheDir?.absolutePath}/audiorecordtest.3gp"

        timer = Timer(this)



        activityMainBinding.run {
            playButton.isEnabled = false
            playButton.alpha = 0.3f
            recordButton.setOnClickListener {
                when (state) {
                    RecodeState.RELEASE -> {
                        recode()
                    }

                    RecodeState.RECODING -> {
                        onRecord(false)
                    }

                    RecodeState.PLAYING -> {

                    }
                }
            }


            playButton.setOnClickListener {
                when (state) {
                    RecodeState.RELEASE -> {
                        onPlay(true)
                    }
                    else -> {
                        //아무것도 안함
                    }
                }
            }

            stopButton.setOnClickListener{
                when (state) {
                    RecodeState.PLAYING -> {
                        onPlay(false)
                    }
                    else ->{
                        //아무것도 안함
                    }
                }
            }
        }
    }

    //녹음 시 권한에 대한 실행 메서드
    fun recode() {
        when {
            ContextCompat.checkSelfPermission(
                this@MainActivity,
                android.Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                onRecord(true)
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                this@MainActivity, android.Manifest.permission.RECORD_AUDIO
            ) -> {
                showPermissionDialog()
            }

            else -> {
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    RECORD_PERMISSION_CODE
                )
            }
        }
    }

    //재생 시 상태에 따른 분기를 위한 메서드
    fun onPlay(start: Boolean) = if (start) startPlaying() else stopPlaying()


    //녹음 시 상태에 따른 분기를 위한 메서드
    fun onRecord(start: Boolean) = if (start) startRecording() else stopRecording()


    //녹음 기능하는 메서드
    fun startRecording() {
        //상태를 녹음중으로 변경
        Log.d("recodingTest", "릴리즈 ->  녹음 시작")
        state = RecodeState.RECODING
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            recoder = MediaRecorder(this).apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(fileName)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            }
        } else {
            recoder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                setOutputFile(fileName)
                setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
            }
        }
        try {
            recoder?.prepare()
        } catch (e: Exception) {
            Log.e("APP", "record prepare() $e")
        }
        recoder?.start()

        activityMainBinding.waveformView.clearData()
        timer.start()

        activityMainBinding.recordButton.apply {
            setImageDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.baseline_stop_24
                )
            )
            imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.black))
        }
        activityMainBinding.playButton.apply {
            isEnabled = false
            alpha = 0.3f
        }
    }

    //녹음 중지 메서드
    fun stopRecording() {
        recoder?.apply {
            stop()
            release()
        }
        recoder = null

        timer.stop()

        state = RecodeState.RELEASE

        activityMainBinding.recordButton.apply {
            setImageDrawable(
                ContextCompat.getDrawable(
                    this@MainActivity,
                    R.drawable.baseline_fiber_manual_record_24
                )
            )
            imageTintList =
                ColorStateList.valueOf(ContextCompat.getColor(this@MainActivity, R.color.red))
        }
        activityMainBinding.playButton.apply {
            isEnabled = true
            alpha = 1.0f
        }
    }

    //재생 메서드
    fun startPlaying() {
        state = RecodeState.PLAYING

        player = MediaPlayer().apply {
            try {
                setDataSource(fileName)
                prepare()
            }catch (e:IOException){
                Log.e("APP", "media player prepare $e")
            }
            start()
        }

        activityMainBinding.waveformView.clearWave()
        timer.start()

        player?.setOnCompletionListener {
            stopPlaying()
        }
        activityMainBinding.recordButton.apply {
            isEnabled = false
            alpha = 0.3f
        }
    }

    //재생 중지 메서드
    fun stopPlaying() {
        state = RecodeState.RELEASE

        player?.release()
        player = null

        timer.stop()

        activityMainBinding.recordButton.apply {
            isEnabled = true
            alpha = 1.0f
        }
    }

    //앱 권한 세팅 화면으로 이동키시는 메서드
    fun navigateToAppSetting() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }

    //교육욕 권한 다이얼로그 띄우는 메서드
    fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setMessage("녹음 권한을 켜야 앱을 사용 할 수 있습니다.")
            .setPositiveButton("권한 허용하기") { dialogInterface: DialogInterface, i: Int ->
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.RECORD_AUDIO),
                    RECORD_PERMISSION_CODE
                )
            }
            .setNegativeButton("취소") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel()
            }.show()
    }

    //권한 설정 화면을 위한 다이얼로그 띄우는 메서드
    fun showPermissionSettiongDialog() {
        AlertDialog.Builder(this)
            .setMessage("녹음 권한을 켜야만 앱을 정상적으로 사용이 가능합니다 앱 설정 화면으로 이동하여 권한을 켜주십시오.")
            .setPositiveButton("권한 설정하러 가기") { dialogInterface: DialogInterface, i: Int ->
                navigateToAppSetting()
            }
            .setNegativeButton("취소") { dialogInterface: DialogInterface, i: Int ->
                dialogInterface.cancel()
            }.show()
    }

    //권한 콜백
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val recordPermissionRequestGranted =
            requestCode == RECORD_PERMISSION_CODE && grantResults.first() == PackageManager.PERMISSION_GRANTED

        if (recordPermissionRequestGranted) {
            onRecord(true)
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this@MainActivity,
                    android.Manifest.permission.RECORD_AUDIO
                )
            ) {
                showPermissionDialog()
            } else {
                showPermissionSettiongDialog()
            }
        }
    }

    override fun onTick(duration: Long) {
        val millisecond = duration % 1000
        val second = (duration / 1000) % 60
        val minute = (duration / 1000 / 60)

        activityMainBinding.timerTextView.text = String.format("%02d:%02d:%02d", minute, second, millisecond / 10)

        if (state == RecodeState.PLAYING){
            activityMainBinding.waveformView.replayAmplitude(duration.toInt())
        }else if(state == RecodeState.RECODING){
            activityMainBinding.waveformView.addAmplitude(recoder?.maxAmplitude?.toFloat() ?: 0f)
        }
    }
}