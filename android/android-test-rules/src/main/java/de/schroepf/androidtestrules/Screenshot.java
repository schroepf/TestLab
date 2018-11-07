package de.schroepf.androidtestrules;

import android.graphics.Bitmap;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface Screenshot {
    String name() default "";

    String subdirectory() default "";

    Bitmap.CompressFormat format() default Bitmap.CompressFormat.PNG;
}
