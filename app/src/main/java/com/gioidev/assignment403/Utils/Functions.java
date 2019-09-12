package com.gioidev.assignment403.Utils;

import android.app.WallpaperManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.gioidev.assignment403.R;

import java.io.IOException;

public class Functions {
        public static void changeMainFragment(FragmentActivity fragmentActivity, Fragment fragment){
            fragmentActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
        }

        public static void changeMainFragmentWithBack(FragmentActivity fragmentActivity, Fragment fragment){
            fragmentActivity.getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
        public static boolean setWallpaper(AppCompatActivity activity, Bitmap bitmap){
            DisplayMetrics metrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int height = metrics.heightPixels;
            int width = metrics.widthPixels;
            Bitmap tempBitmap = scaleCenterCrop(bitmap, height, width);
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
            try {
                wallpaperManager.setBitmap(bitmap);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        public static Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
            int sourceWidth = source.getWidth();
            int sourceHeight = source.getHeight();

            float xScale = (float) newWidth / sourceWidth;
            float yScale = (float) newHeight / sourceHeight;
            float scale = Math.max(xScale, yScale);
            float scaledWidth = scale * sourceWidth;
            float scaledHeight = scale * sourceHeight;

            float left = (newWidth - scaledWidth) / 2;
            float top = (newHeight - scaledHeight) / 2;
            RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

            // Finally, we create a new bitmap of the specified size and draw our new,
            // scaled bitmap onto it.
            Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
            Canvas canvas = new Canvas(dest);
            canvas.drawBitmap(source, null, targetRect, null);

            return dest;
        }

}
