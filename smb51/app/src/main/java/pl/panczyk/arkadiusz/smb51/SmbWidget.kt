package pl.panczyk.arkadiusz.smb51

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews


class SmbWidget : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.i("widgetOnReceive", "onReceive called")
        Log.i("widgetOnReceive", "${intent?.action}")

        val views = RemoteViews(context?.packageName, R.layout.smb_widget)

        when(intent?.action) {
            LEFT_ACTION_BUTTON -> {
                currentImageView = if (currentImageView == 0) imageViewList.size - 1 else currentImageView - 1
                views.setImageViewResource(R.id.imageView, imageViewList[currentImageView])
                reloadView(context, views)
            }
            RIGHT_ACTION_BUTTON -> {
                currentImageView = (currentImageView + 1) % imageViewList.size
                views.setImageViewResource(R.id.imageView, imageViewList[currentImageView])
                reloadView(context, views)
            }
            BEFORE_ACTION_BUTTON -> {
                currentMusic = if (currentMusic == 0) musicList.size - 1 else currentMusic - 1
                reloadMusic(context!!, currentMusic)
            }
            AFTER_ACTION_BUTTON -> {
                currentMusic = (currentMusic + 1) % musicList.size
                reloadMusic(context!!, currentMusic)
            }
            PAUSE_ACTION_BUTTON -> if (mediaPlayer.isPlaying) mediaPlayer.pause() else mediaPlayer.start()
        }
    }

    private fun reloadMusic(context: Context, currentMusic: Int) {
        mediaPlayer.stop()
        mediaPlayer = MediaPlayer.create(context, musicList[Companion.currentMusic])
        mediaPlayer.start()
    }

    private fun reloadView(context: Context?, views: RemoteViews) {
        val manager = AppWidgetManager.getInstance(context)
        val name = context?.let { ComponentName(it, SmbWidget::class.java) }
        val ids = AppWidgetManager.getInstance(context).getAppWidgetIds(name)
        manager.updateAppWidget(ids, views)
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        val views = RemoteViews(context.packageName, R.layout.smb_widget)

        // Init photos
        views.setImageViewResource(R.id.imageView, imageViewList[currentImageView])
        views.setImageViewResource(R.id.imageView2, imageViewList[2])

        // Browser button
        views.setOnClickPendingIntent(
            R.id.browserBtn,
            pendingIntentOf(
                context,
                Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))
            )
        )

        // Image buttons
        views.setOnClickPendingIntent(R.id.leftButton, getPendingSelfIntent(context, LEFT_ACTION_BUTTON))
        views.setOnClickPendingIntent(R.id.rightButton, getPendingSelfIntent(context, RIGHT_ACTION_BUTTON))

        // Music buttons
        try {
            mediaPlayer = MediaPlayer.create(context, R.raw.music1)
            mediaPlayer.prepare()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        views.setOnClickPendingIntent(R.id.beforeButton, getPendingSelfIntent(context, BEFORE_ACTION_BUTTON))
        views.setOnClickPendingIntent(R.id.afterButton, getPendingSelfIntent(context, AFTER_ACTION_BUTTON))
        views.setOnClickPendingIntent(R.id.startButton, getPendingSelfIntent(context, PAUSE_ACTION_BUTTON))

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun pendingIntentOf(context: Context, intent: Intent) =
        PendingIntent.getActivity(
            context,
            1,
            intent,
            PendingIntent.FLAG_MUTABLE
        )

    private fun getPendingSelfIntent(context: Context?, action: String?): PendingIntent? {
        val intent = Intent(context, SmbWidget::class.java)
        intent.action = action
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_MUTABLE)
    }

    companion object {
        var currentImageView = 0
        var currentMusic = 0
        lateinit var mediaPlayer: MediaPlayer

        val imageViewList: List<Int> = listOf(R.drawable.kotlin, R.drawable.java, R.drawable.jdk)
        val musicList: List<Int> = listOf(R.raw.music1, R.raw.music2, R.raw.music3)

        const val LEFT_ACTION_BUTTON = "pl.panczyk.arkadiusz.smb51.leftButton"
        const val RIGHT_ACTION_BUTTON = "pl.panczyk.arkadiusz.smb51.rightButton"
        const val BEFORE_ACTION_BUTTON = "pl.panczyk.arkadiusz.smb51.beforeButton"
        const val AFTER_ACTION_BUTTON = "pl.panczyk.arkadiusz.smb51.afterButton"
        const val PAUSE_ACTION_BUTTON = "pl.panczyk.arkadiusz.smb51.pauseButton"
    }
}


