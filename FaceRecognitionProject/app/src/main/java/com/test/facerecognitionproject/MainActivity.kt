package com.test.facerecognitionproject


import android.graphics.PointF
import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.TransitionManager
import android.util.SizeF
import android.view.View
import com.test.face_recognition.Camera
import com.test.face_recognition.recognition.FaceAnalyzerListener
import com.test.facerecognitionproject.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), FaceAnalyzerListener {
    private lateinit var activityMainBinding: ActivityMainBinding
    private val camera = Camera(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)
        setProgressText("시작하기를 눌러주세요")
        camera.initCamera(activityMainBinding.cameraLayout, this)

        activityMainBinding.startDetectButton.setOnClickListener {
            it.visibility = View.GONE
            activityMainBinding.faceOverlayView.reset()
            camera.startFaceDetect()
            setProgressText("알굴을 보여주세요")
        }



    }

    override fun detect() {
        
    }

    override fun stopDetect() {
        camera.stopFaceDetect()
        reset()
    }

    override fun notDetect() {
        activityMainBinding.faceOverlayView.reset()
    }

    override fun detectProgress(progress: Float, message: String) {
        setProgressText(message)
    }

    override fun faceSize(rectF: RectF, sizeF: SizeF, pointF: PointF) {
        activityMainBinding.faceOverlayView.setSize(rectF, sizeF, pointF)
    }

    private fun reset(){
        activityMainBinding.startDetectButton.visibility = View.VISIBLE
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        camera.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun setProgressText(text: String) {
        //애니메이션 설정
        TransitionManager.beginDelayedTransition(activityMainBinding.root)
        activityMainBinding.progressTextView.text = text
    }
}