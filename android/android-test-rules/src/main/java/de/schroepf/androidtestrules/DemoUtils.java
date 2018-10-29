package de.schroepf.androidtestrules;

import android.content.Intent;

import javax.annotation.Nullable;

import de.schroepf.androidtestrules.demo.BarsStyle;
import de.schroepf.androidtestrules.demo.BluetoothState;
import de.schroepf.androidtestrules.demo.MobileDataType;
import de.schroepf.androidtestrules.demo.VolumeState;

/**
 * Helper class to create {@link Intent}s to send demo mode commands as documented in:
 * https://android.googlesource.com/platform/frameworks/base/+show/master/packages/SystemUI/docs/demo_mode.md
 */
public class DemoUtils {
    private DemoUtils() {
        // static helper class - do not instantiate!
    }

    public static Intent createDemoIntent() {
        return new Intent("com.android.systemui.demo");
    }

    public static Intent createEnterDemoModeIntent() {
        Intent intent = createDemoIntent();
        intent.putExtra("command", "enter");
        return intent;
    }

    public static Intent createExitDemoModeIntent() {
        Intent intent = createDemoIntent();
        intent.putExtra("command", "exit");
        return intent;
    }

    @Nullable
    public static Intent createBatteryIntent(@Nullable Integer level, @Nullable Boolean plugged) {
        if (level == null && plugged == null) {
            return null;
        }

        Intent intent = createDemoIntent();
        intent.putExtra("command", "battery");
        if (level != null) {
            intent.putExtra("level", level.toString());
        }

        if (plugged != null) {
            intent.putExtra("plugged", plugged ? "true" : "false");
        }
        return intent;
    }

    @Nullable
    public static Intent createNetworkIntent(
            @Nullable Boolean airplane,
            @Nullable Boolean fully,
            @Nullable Boolean wifi,
            @Nullable Integer wifiLevel,
            @Nullable Boolean mobile,
            @Nullable MobileDataType mobileDataType,
            @Nullable Integer mobileLevel,
            @Nullable Boolean carriernetworkchange,
            @Nullable Integer sims,
            @Nullable Boolean nosim
    ) {
        if (airplane == null
                && fully == null
                && wifi == null
                && wifiLevel == null
                && mobile == null
                && mobileDataType == null
                && mobileLevel == null
                && carriernetworkchange == null
                && sims == null
                && nosim == null) {
            return null;
        }

        Intent intent = createDemoIntent();
        intent.putExtra("command", "network");

        if (airplane != null) {
            intent.putExtra("airplane", airplane ? "show" : "hide");
        }

        if (fully != null) {
            intent.putExtra("fully", fully ? "true" : "false");
        }

        if (wifi != null) {
            intent.putExtra("wifi", wifi ? "show" : "hide");
        }

        if (wifiLevel != null) {
            intent.putExtra("level", wifiLevel.toString());
        }

        if (mobile != null) {
            intent.putExtra("mobile", mobile ? "show" : "hide");
        }

        if (mobileDataType != null) {
            switch (mobileDataType) {
                case ONE_X:
                    intent.putExtra("datatype", "1x");
                    break;

                case THREE_G:
                    intent.putExtra("datatype", "3g");
                    break;

                case FOUR_G:
                    intent.putExtra("datatype", "4g");
                    break;

                case E:
                    intent.putExtra("datatype", "e");
                    break;

                case G:
                    intent.putExtra("datatype", "g");
                    break;

                case H:
                    intent.putExtra("datatype", "h");
                    break;

                case LTE:
                    intent.putExtra("datatype", "lte");
                    break;

                case ROAM:
                    intent.putExtra("datatype", "roam");
                    break;

                case GONE:
                    intent.putExtra("datatype", "hide");
                    break;
            }
        }

        if (mobileLevel != null) {
            intent.putExtra("level", mobileLevel.toString());
        }

        if (carriernetworkchange != null) {
            intent.putExtra("carriernetworkchange", carriernetworkchange ? "show" : "hide");
        }

        if (sims != null) {
            intent.putExtra("sims", sims.toString());
        }

        if (nosim != null) {
            intent.putExtra("nosim", nosim ? "show" : "hide");
        }

        return intent;
    }

    @Nullable
    public static Intent createBarsIntent(@Nullable BarsStyle barsStyle) {
        if (barsStyle == null) {
            return null;
        }

        Intent intent = createDemoIntent();
        intent.putExtra("command", "bars");

        switch (barsStyle) {
            case OPAQUE:
                intent.putExtra("mode", "opaque");
                break;

            case TRANSLUCENT:
                intent.putExtra("mode", "translucent");
                break;

            case SEMI_TRANSPARENT:
                intent.putExtra("mode", "semi-transparent");
                break;
        }

        return intent;
    }

    @Nullable
    public static Intent createStatusIntent(
            @Nullable VolumeState volumeState,
            @Nullable BluetoothState bluetoothState,
            @Nullable Boolean location,
            @Nullable Boolean alarm,
            @Nullable Boolean sync,
            @Nullable Boolean tty,
            @Nullable Boolean eri,
            @Nullable Boolean mute,
            @Nullable Boolean speakerphone
    ) {
        if (volumeState == null
                && bluetoothState == null
                && location == null
                && alarm == null
                && sync == null
                && tty == null
                && eri == null
                && mute == null
                && speakerphone == null) {
            return null;
        }

        Intent intent = createDemoIntent();
        intent.putExtra("command", "status");

        if (volumeState != null) {
            switch (volumeState) {
                case GONE:
                    intent.putExtra("volume", "hide");
                    break;
                case SILENT:
                    intent.putExtra("volume", "silent");
                    break;
                case VIBRATE:
                    intent.putExtra("volume", "vibrate");
                    break;
            }
        }

        if (bluetoothState != null) {
            switch (bluetoothState) {
                case GONE:
                    intent.putExtra("bluetooth", "hide");
                    break;
                case CONNECTED:
                    intent.putExtra("bluetooth", "connected");
                    break;
                case DISCONNECTED:
                    intent.putExtra("bluetooth", "disconnected");
                    break;
            }
        }

        if (location != null) {
            intent.putExtra("location", location ? "show" : "hide");
        }

        if (alarm != null) {
            intent.putExtra("alarm", alarm ? "show" : "hide");
        }

        if (sync != null) {
            intent.putExtra("sync", sync ? "show" : "hide");
        }

        if (tty != null) {
            intent.putExtra("tty", tty ? "show" : "hide");
        }

        if (eri != null) {
            intent.putExtra("eri", eri ? "show" : "hide");
        }

        if (mute != null) {
            intent.putExtra("mute", mute ? "show" : "hide");
        }

        if (speakerphone != null) {
            intent.putExtra("speakerphoone", speakerphone ? "show" : "hide");
        }

        return intent;
    }

    @Nullable
    public static Intent createNotificationsIntent(@Nullable Boolean visible) {
        if (visible == null) {
            return null;
        }

        Intent intent = createDemoIntent();
        intent.putExtra("command", "notifications");
        intent.putExtra("visible", visible ? "true" : "false");
        return intent;
    }

    @Nullable
    public static Intent createClockIntent(@Nullable String hhmm, @Nullable Long millis) {
        if (hhmm == null && millis == null) {
            return null;
        }

        Intent intent = createDemoIntent();
        intent.putExtra("command", "clock");
        if (hhmm != null) {
            intent.putExtra("hhmm", hhmm);
        }

        if (millis != null) {
            intent.putExtra("millis", millis.toString());
        }

        return intent;
    }
}
