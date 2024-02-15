package com.test.face_recognition

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.Executors

class Camera(private val context: Context): ActivityCompat.OnRequestPermissionsResultCallback {

    //객체를 생성하고 미리보기 화면을 제공할 SurfaceProvider를 설정하여 카메라 미리보기를 준비
    private val preview by lazy {
        Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(previewView.surfaceProvider)
            }
    }

    private val cameraSelector by lazy {
        CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()
    }

    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private lateinit var previewView: PreviewView

    private fun permissionCheck(context: Context) {
        val permissionList = listOf(Manifest.permission.CAMERA)
        if (!PermissionUtil.checkPermission(context,permissionList)){
            PermissionUtil.requestPermission(context as Activity, permissionList)
        } else {
            openPreview()
        }
    }
    //카메라 동작을 실행할 스레드 풀
    private var cameraExecutor = Executors.newSingleThreadExecutor()

    //카메라 미리보기를 표시할 PreviewView를 초기화하고 주어진 layout에 추가
    fun initCamera(layout: ViewGroup) {
        previewView = PreviewView(context)
        layout.addView(previewView)
        permissionCheck(context)
    }

    //카메라 프로바이더를 비동기적으로 초기화하고, 초기화가 완료되면 메인 스레드에서 startPreview 함수를 호출하여 미리보기를 시작
    private fun openPreview() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            .also { providerFuture ->
                providerFuture.addListener({

                },ContextCompat.getMainExecutor(context))
            }
    }

    //카메라 프로바이더에서 카메라를 얻어와서 프리뷰를 설정
    private fun startPreview(context: Context) {
        val cameraProvider = cameraProviderFuture.get()
        try{
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                context as LifecycleOwner,
                cameraSelector,
                preview
            )
        }catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        var flag = true
        if (grantResults.isNotEmpty()){
            for ((i,_) in permissions.withIndex()){
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                    flag = false
                }
            }
            if (flag){
                openPreview()
            }else{
                Toast.makeText(context, "권한을 허용해야 합니다", Toast.LENGTH_SHORT).show()
                (context as Activity).finish()
            }
        }
    }
}