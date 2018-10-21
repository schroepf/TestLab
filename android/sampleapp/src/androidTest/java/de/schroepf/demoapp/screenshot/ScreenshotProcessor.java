package de.schroepf.demoapp.screenshot;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.ScreenCaptureProcessor;

public class ScreenshotProcessor implements ScreenCaptureProcessor {
    private static final String TAG = "ScreenshotProcessor";

    @Override
    public String process(ScreenCapture capture) throws IOException {
        if (capture == null) {
            return null;
        }

        return save(InstrumentationRegistry.getInstrumentation().getTargetContext(), capture.getName(), "test", capture.getBitmap(), capture.getFormat());
    }

    private String save(Context context, String name, String subdirectory, Bitmap bitmap, Bitmap.CompressFormat compressFormat) throws IOException {
        File dir = new File(context.getExternalFilesDir(null), subdirectory);

        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Could not create screenshot directory: " + dir.getPath());
        }

        File imageFile = new File(dir, name + extensionForFormat(compressFormat));

        OutputStream out = null;

        try {
            out = new FileOutputStream(imageFile);
            bitmap.compress(compressFormat, 100, out);
            out.flush();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
                Log.e(TAG, "Unable to save screenshot", e);
            }

            Log.d(TAG, "Screenshot saved as: " + imageFile.getAbsolutePath());
            if (compressFormat == Bitmap.CompressFormat.JPEG) {
                // ExifInterface only supports specific formats, but
                addMetadata(imageFile);
            }
        }

        return imageFile.getName();
    }

    @Nullable
    private String extensionForFormat(Bitmap.CompressFormat format) {
        switch (format) {
            case JPEG:
                return ".jpg";

            case PNG:
                return ".png";

            case WEBP:
                return ".webp";
        }

        return null;
    }

    private void addMetadata(File imageFile) throws IOException {
        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
        exif.setAttribute(ExifInterface.TAG_MAKE, Build.MANUFACTURER);
        exif.setAttribute(ExifInterface.TAG_MODEL, Build.MODEL);
        exif.setAttribute(ExifInterface.TAG_DATETIME, exifDateTime());
        exif.saveAttributes();
    }

    @Nonnull
    private String exifDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
        return simpleDateFormat.format(new Date());
    }
}