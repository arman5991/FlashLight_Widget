package com.flashlight;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.widget.RemoteViews;
import android.widget.Toast;

public class FlashlightWidgetReceiver extends BroadcastReceiver {
    public static boolean isLightOn = false;
    public static Camera camera;

    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.activity_widget);

        if (isLightOn) {
            views.setImageViewResource(R.id.clickMe, R.drawable.off);
        } else {
            views.setImageViewResource(R.id.clickMe, R.drawable.on);
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(new ComponentName(context, WidgetActivity.class), views);

        if (isLightOn) {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
                isLightOn = false;
            }

        } else {
            camera = Camera.open();

            if (camera == null) {
                Toast.makeText(context, "no flashLight", Toast.LENGTH_SHORT).show();
            } else {
                // Set the torch flash mode
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                try {
                    camera.setParameters(param);
                    camera.startPreview();
                    isLightOn = true;
                } catch (Exception e) {
                    Toast.makeText(context, "no flashLight", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}