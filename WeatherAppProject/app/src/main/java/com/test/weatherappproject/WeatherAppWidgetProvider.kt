package com.test.weatherappproject

import android.app.ForegroundServiceStartNotAllowedException
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import java.lang.Exception

class WeatherAppWidgetProvider: AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)

        // Perform this loop procedure for each App Widget that belongs to this provider
        appWidgetIds.forEach { appWidgetId ->
            // Create an Intent to launch ExampleActivity
            val pendingIntent: PendingIntent = Intent(context, UpdateWeatherService::class.java)
                .let { intent ->
                    PendingIntent.getForegroundService(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)
                }

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.widget_weather
            ).apply {
                setOnClickPendingIntent(R.id.temperatureTexView, pendingIntent)
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
        //업데이트를 수행하는 서비스를 실행
        val serviceIntent = Intent(context, UpdateWeatherService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            try {
                ContextCompat.startForegroundService(context,serviceIntent)
            }catch (e:ForegroundServiceStartNotAllowedException){
                e.printStackTrace()
            }
        }else{
            ContextCompat.startForegroundService(context,serviceIntent)
        }
    }
}