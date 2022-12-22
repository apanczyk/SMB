package pl.panczyk.arkadiusz.smb51

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast

/**
 * Implementation of App Widget functionality.
 */
class SmbWidget : AppWidgetProvider() {

    private var requestCode = 0

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, requestCode++)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.i("widgetOnReceive", "onReceive called")
        if(intent?.action == "pl.panczyk.arkadiusz.smb51.ActionClick")
            Toast.makeText(context, "Clicked!", Toast.LENGTH_SHORT).show()
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    requestCode: Int
) {
    Log.i("widgetOnReceive", "onReceive called")
    val widgetText = context.getString(R.string.appwidget_text)
    val views = RemoteViews(context.packageName, R.layout.smb_widget)
    views.setTextViewText(R.id.appwidget_text, widgetText)

    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.pja.edu.pl"))
    val pendingIntent = PendingIntent.getActivity(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_MUTABLE
    )
    views.setOnClickPendingIntent(R.id.browserBtn, pendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}