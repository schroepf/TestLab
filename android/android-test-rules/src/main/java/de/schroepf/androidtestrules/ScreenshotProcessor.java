package de.schroepf.androidtestrules;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.InstrumentationRegistry;
import androidx.test.runner.screenshot.ScreenCapture;
import androidx.test.runner.screenshot.ScreenCaptureProcessor;

public class ScreenshotProcessor implements ScreenCaptureProcessor {

    @Nullable
    private final String subdirectory;

    ScreenshotProcessor(@Nullable String subdirectory) {
        this.subdirectory = subdirectory;
    }

    @Override
    public String process(ScreenCapture capture) throws IOException {
        if (capture == null) {
            return null;
        }

        return save(InstrumentationRegistry.getInstrumentation().getTargetContext(), capture.getName(), subdirectory, capture.getBitmap(), capture.getFormat());
    }

    private String save(Context context, String name, @Nullable String subdirectory, Bitmap bitmap, Bitmap.CompressFormat compressFormat) throws IOException {
        File dir = subdirectory != null ? new File(context.getExternalFilesDir(null), subdirectory) : context.getExternalFilesDir(null);

        if (!dir.exists() && !dir.mkdirs()) {
            throw new IOException("Could not create screenshot directory: " + dir.getPath());
        }

        File imageFile = new File(dir, name + extensionForFormat(compressFormat));

        try {
            OutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(compressFormat, 100, out);
            out.flush();
        } finally {
            if (compressFormat == Bitmap.CompressFormat.JPEG) {
                // ExifInterface only supports specific formats, but
                addMetadata(imageFile);
            }
        }

        return imageFile.getName();
    }

    @NonNull
    private String extensionForFormat(Bitmap.CompressFormat format) {
        switch (format) {
            case JPEG:
                return ".jpg";

            case PNG:
                return ".png";

            case WEBP:
                return ".webp";
        }

        // should never happen
        return "";
    }

    private void addMetadata(@NonNull File imageFile) throws IOException {
        ExifInterface exif = new ExifInterface(imageFile.getAbsolutePath());
        exif.setAttribute(ExifInterface.TAG_MAKE, Build.MANUFACTURER);
        exif.setAttribute(ExifInterface.TAG_MODEL, Build.MODEL);
        exif.setAttribute(ExifInterface.TAG_DATETIME, exifDateTime());
        exif.saveAttributes();
    }

    @NonNull
    private String exifDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.US);
        return simpleDateFormat.format(new Date());
    }
}