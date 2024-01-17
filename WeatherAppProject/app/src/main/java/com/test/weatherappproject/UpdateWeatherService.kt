package com.test.weatherappproject

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.LocationServices
import com.test.weatherappproject.repository.WeatherRepository


class UpdateWeatherService : Service() {

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        //서비스를 실행하고 위젯을 업데이트하는 작업은 알림이 필수적이므로 알림을 띄우는 작업 시행
        createChannel()
        startForeground(1, createNotification())

        //위젯 매니저 가져오기
        val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            val pendingIntent: PendingIntent = Intent(this,SettingActivity::class.java).let{
                PendingIntent.getActivity(this,2,it,PendingIntent.FLAG_IMMUTABLE)
            }

            RemoteViews(packageName,R.layout.widget_weather).apply {
                setTextViewText(R.id.temperatureTexView,"권한 없음")
                setTextViewText(R.id.weatherTextView, "")
                setOnClickPendingIntent(R.id.temperatureTexView,pendingIntent)
            }.also {remoteViews ->
                val appWidgetName = ComponentName(this, WeatherAppWidgetProvider::class.java)
                appWidgetManager.updateAppWidget(appWidgetName,remoteViews)
            }
            stopSelf()
            //TODO 위젯을 권한없음 상태로 표시하고 클릭 시 권한 팝업을 띄울 수 있도록 조정
            return super.onStartCommand(intent, flags, startId)
        }
        //내 위치를 불러온 다음 통신을 그대로 시행하되 얻어온 리스트로 앱 위젯의 ui인자를 업데이트 하고 pendingIntent를 서비스와 연결시킨다
        //
        LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
            WeatherRepository.getVillageForecast(
                lon = it.longitude,
                lat = it.latitude,
                successCallback = { forecastList ->

                    val pendingServiceIntent: PendingIntent =
                        Intent(this, UpdateWeatherService::class.java).let {
                            PendingIntent.getService(this, 1, it, PendingIntent.FLAG_IMMUTABLE)
                        }

                    val currentForecast = forecastList.first()
                    RemoteViews(packageName, R.layout.widget_weather).apply {
                        setTextViewText(
                            R.id.temperatureTexView,
                            getString(R.string.temperature_text, currentForecast.temperature)
                        )
                        setTextViewText(R.id.weatherTextView, currentForecast.weather)
                        setOnClickPendingIntent(R.id.temperatureTexView, pendingServiceIntent)
                    }.also { remoteViews ->
                        val widgetName = ComponentName(this, WeatherAppWidgetProvider::class.java)
                        appWidgetManager.updateAppWidget(widgetName, remoteViews)
                    }

                    //서비스 종료
                    stopSelf()
                },
                failCallback = { exception ->
                    val pendingServiceIntent: PendingIntent =
                        Intent(this, UpdateWeatherService::class.java).let {
                            PendingIntent.getService(this, 1, it, PendingIntent.FLAG_IMMUTABLE)
                        }

                    RemoteViews(packageName, R.layout.widget_weather).apply {
                        setTextViewText(
                            R.id.temperatureTexView,
                           "에러"
                        )
                        setTextViewText(R.id.weatherTextView, "")
                        setOnClickPendingIntent(R.id.temperatureTexView, pendingServiceIntent)
                    }.also { remoteViews ->
                        val widgetName = ComponentName(this, WeatherAppWidgetProvider::class.java)
                        appWidgetManager.updateAppWidget(widgetName, remoteViews)
                    }
                    exception.printStackTrace()
                    stopSelf()
                }
            )
        }

        return super.onStartCommand(
            intent, flags, startId
        )

    }

    private fun createChannel() {
        val channel = NotificationChannel(
            "widget_refresh_channel",
            "날씨앱",
            NotificationManager.IMPORTANCE_LOW
        )

        channel.description = "위젯을 업데이트하는 채널"

        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("날씨앱")
            .setContentText("날씨앱 업데이트 됨...")
            .build()
    }

    companion object {
        const val NOTIFICATION_CHANNEL = "widget_refresh_channel"
    }

    override fun onDestroy() {
        super.onDestroy()
        stopForeground(STOP_FOREGROUND_REMOVE)
    }
}