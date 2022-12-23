package pl.panczyk.arkadiusz.smb51

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.Toast


class SmbWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            createBrowser(context, appWidgetManager, appWidgetId)
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

internal fun createBrowser(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    var views = RemoteViews(context.packageName, R.layout.smb_widget)
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
    val pendingIntent = PendingIntent.getActivity(
        context,
        1,
        intent,
        PendingIntent.FLAG_MUTABLE
    )
    views.setOnClickPendingIntent(R.id.browserBtn, pendingIntent)
    views.setImageViewResource(R.id.imageView, R.drawable.example_appwidget_preview)
    views.setImageViewResource(R.id.imageView2, R.drawable.example_appwidget_preview)
    appWidgetManager.updateAppWidget(appWidgetId, views)
}
