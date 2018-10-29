package de.schroepf.androidtestrules;

import android.app.UiAutomation;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import de.schroepf.androidtestrules.demo.BarsStyle;
import de.schroepf.androidtestrules.demo.BluetoothState;
import de.schroepf.androidtestrules.demo.MobileDataType;
import de.schroepf.androidtestrules.demo.VolumeState;

public class DemoModeRule implements TestRule {

    @NonNull
    private final List<Intent> demoIntents;

    private DemoModeRule(@NonNull List<Intent> demoIntents) {
        this.demoIntents = demoIntents;
    }

    @Override
    public Statement apply(final Statement base, Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                enableDemoMode();
                try {
                    base.evaluate();
                } finally {
                    disableDemoMode();
                }
            }
        };
    }

    private void enableDemoMode() {
        Context context = InstrumentationRegistry.getTargetContext();
        UiAutomation automation = InstrumentationRegistry.getInstrumentation().getUiAutomation();

        // make sure permission is granted
        ScreenshotUtils.executeShellCommand(automation, "pm grant " + context.getPackageName() + " android.permission.DUMP");

        // enable demo mode
        ScreenshotUtils.executeShellCommand(automation, "settings put global sysui_demo_allowed 1");

        // enter demo mode
        context.sendBroadcast(DemoUtils.createEnterDemoModeIntent());

        for (Intent intent : demoIntents) {
            context.sendBroadcast(intent);
        }
    }

    private void disableDemoMode() {
        Context context = InstrumentationRegistry.getTargetContext();
        UiAutomation automation = InstrumentationRegistry.getInstrumentation().getUiAutomation();

        // exit demo mode
        context.sendBroadcast(DemoUtils.createExitDemoModeIntent());

        // disable demo mode
        ScreenshotUtils.executeShellCommand(automation, "settings put global sysui_demo_allowed 0");
    }


    public static final class Builder {

        @Nullable
        private String clockHhmm;

        @Nullable
        private Long clockMillis;

        @Nullable
        private Integer batteryLevel;

        @Nullable
        private Boolean batteryPlugged;

        @Nullable
        private VolumeState volumeState;

        @Nullable
        private BluetoothState bluetoothState;

        @Nullable
        private Boolean location;

        @Nullable
        private Boolean alarm;

        @Nullable
        private Boolean sync;

        @Nullable
        private Boolean tty;

        @Nullable
        private Boolean eri;

        @Nullable
        private Boolean mute;

        @Nullable
        private Boolean speakerphone;

        @Nullable
        private Boolean notifications;

        @Nullable
        private Boolean airplane;

        @Nullable
        private Boolean fully;

        @Nullable
        private Boolean wifi;

        @Nullable
        private Integer wifiLevel;

        @Nullable
        private Boolean mobile;

        @Nullable
        private MobileDataType mobileDataType;

        @Nullable
        private Integer mobileLevel;

        @Nullable
        private Boolean carriernetworkchange;

        @Nullable
        private Integer sims;

        @Nullable
        private Boolean nosim;

        @Nullable
        private BarsStyle barsStyle;

        public Builder() {
        }

        /**
         * Control the battery display
         * <p>
         * If the given level value is not in the range [0..100] it will be ignored.
         *
         * @param level set the battery level (0 - 100)
         * @return the updated builder
         */
        public Builder batteryLevel(int level) {
            if (level < 0 || level > 100) {
                return this;
            }

            this.batteryLevel = level;
            return this;
        }

        /**
         * Control the battery display
         *
         * @param plugged set charging state to plugged or unplugged
         * @return the updated builder
         */
        public Builder batteryPlugged(boolean plugged) {
            this.batteryPlugged = plugged;
            return this;
        }

        /**
         * Control if the airplane icon shall be displayed
         *
         * @param airplane {@code true} to display, {@code false} to hide
         * @return the updated builder
         */
        public Builder airplane(boolean airplane) {
            this.airplane = airplane;
            return this;
        }

        /**
         * Sets MCS state to fully connected.
         *
         * @param fully {@code true} for fully connected MCS state
         * @return the updated builder
         */
        public Builder fully(boolean fully) {
            this.fully = fully;
            return this;
        }

        /**
         * Control whether or not the wifi symbol is displayed.
         *
         * @param wifi {@code true} to show the wifi icon, {@code false} to hide it
         * @return the updated builder
         */
        public Builder wifi(boolean wifi) {
            this.wifi = wifi;
            return this;
        }

        /**
         * Control the level of the wifi network
         * <p>
         * If the given value is not in the range [0..4] it will be ignored.
         *
         * @param wifiLevel the wifi level in the range (0..4)
         * @return the updated builder
         */
        public Builder wifiLevel(int wifiLevel) {
            if (wifiLevel < 0 || wifiLevel > 4) {
                return this;
            }

            this.wifiLevel = wifiLevel;
            return this;
        }

        /**
         * Control if the mobile network icon is shown or not
         *
         * @param mobile {@code true} to show or {@code false} to hide
         * @return the updated builder
         */
        public Builder mobile(boolean mobile) {
            this.mobile = mobile;
            return this;
        }

        /**
         * The mobile network type
         *
         * @param mobileDataType the mobile network type
         * @return the updated builder
         */
        public Builder mobileDataType(MobileDataType mobileDataType) {
            this.mobileDataType = mobileDataType;
            return this;
        }

        /**
         * Sets mobile signal strength level
         * <p>
         * If the given value is not in the range [0..4] it will be ignored.
         *
         * @param level the mobile network level
         * @return the updated builder
         */
        public Builder mobileLevel(int level) {
            if (level < 0 || level > 4) {
                return this;
            }

            this.mobileLevel = level;
            return this;
        }

        /**
         * Sets mobile signal icon to carrier network change UX when disconnected
         *
         * @param carriernetworkchange whether or not to show the carrier network change UX
         * @return the updated builder
         */
        public Builder carriernetworkchange(boolean carriernetworkchange) {
            this.carriernetworkchange = carriernetworkchange;
            return this;
        }

        /**
         * Set the amount of sims
         * <p>
         * If the given value is out of range [1-8] it will be ignored.
         *
         * @param sims the number of sims (1-8)
         * @return the updated builder
         */
        public Builder sims(int sims) {
            if (sims < 1 || sims > 8) {
                return this;
            }

            this.sims = sims;
            return this;
        }

        /**
         * Control if the no sim icon is shown
         *
         * @param nosim whether to show or hide the icon
         * @return the updated builder
         */
        public Builder nosim(boolean nosim) {
            this.nosim = nosim;
            return this;
        }

        /**
         * Control the visual style of the bars
         *
         * @param barsStyle set the bars visual style (opaque, translucent, semi-transparent)
         * @return the updated builder
         */
        public Builder bars(BarsStyle barsStyle) {
            this.barsStyle = barsStyle;
            return this;
        }

        /**
         * Sets the icon in the volume slot
         *
         * @param volumeState the state of the volume icon
         * @return the updated builder
         */
        public Builder volume(VolumeState volumeState) {
            this.volumeState = volumeState;
            return this;
        }

        /**
         * Sets the icon in the bluetooth slot
         *
         * @param bluetoothState the state of the bluetooth icon
         * @return the updated builder
         */
        public Builder bluetooth(BluetoothState bluetoothState) {
            this.bluetoothState = bluetoothState;
            return this;
        }

        /**
         * Sets the icon in the location slot
         *
         * @param location whether or not the location icon should be visible
         * @return the updated builder
         */
        public Builder location(boolean location) {
            this.location = location;
            return this;
        }

        /**
         * Sets the icon in the alarm_clock slot
         *
         * @param alarm whether or not the alarm icon should be visible
         * @return the updated builder
         */
        public Builder alarm(boolean alarm) {
            this.alarm = alarm;
            return this;
        }

        /**
         * Sets the icon in the sync_active slot
         *
         * @param sync whether or not the sync icon should be visible
         * @return the updated builder
         */
        public Builder sync(boolean sync) {
            this.sync = sync;
            return this;
        }

        /**
         * Sets the icon in the tty slot
         *
         * @param tty whether or not the tty icon should be visible
         * @return the updated builder
         */
        public Builder tty(boolean tty) {
            this.tty = tty;
            return this;
        }

        /**
         * Sets the icon in the cdma_eri slot
         *
         * @param eri whether or not the eri icon should be visible
         * @return the updated builder
         */
        public Builder eri(boolean eri) {
            this.eri = eri;
            return this;
        }

        /**
         * Sets the icon in the mute slot
         *
         * @param mute whether or not the mute icon should be visible
         * @return the updated builder
         */
        public Builder mute(boolean mute) {
            this.mute = mute;
            return this;
        }

        /**
         * Sets the icon in the speakerphone slot
         *
         * @param speakerphone whether or not speakerphone should be visible
         * @return the updated builder
         */
        public Builder speakerphone(boolean speakerphone) {
            this.speakerphone = speakerphone;
            return this;
        }

        /**
         * Control the notification icons
         *
         * @param visible whether or not notifications should be visible
         * @return the updated builder
         */
        public Builder notifications(boolean visible) {
            this.notifications = visible;
            return this;
        }

        /**
         * Control the clock display
         *
         * @param hhmm the time in "hhmm" format (e.g. "0815" for "8:15")
         * @return the updated builder
         */
        public Builder clock(String hhmm) {
            this.clockHhmm = hhmm;
            return this;
        }

        /**
         * Control the clock display
         *
         * @param millis the time in milliseconds
         * @return the updated builder
         */
        public Builder clock(long millis) {
            this.clockMillis = millis;
            return this;
        }

        public DemoModeRule build() {
            List<Intent> intents = new ArrayList<>();

            addIfNotNull(intents, DemoUtils.createBatteryIntent(batteryLevel, batteryPlugged));
            addIfNotNull(intents, DemoUtils.createNetworkIntent(airplane, fully, wifi, wifiLevel, mobile, mobileDataType, mobileLevel, carriernetworkchange, sims, nosim));
            addIfNotNull(intents, DemoUtils.createBarsIntent(barsStyle));
            addIfNotNull(intents, DemoUtils.createStatusIntent(volumeState, bluetoothState, location, alarm, sync, tty, eri, mute, speakerphone));
            addIfNotNull(intents, DemoUtils.createNotificationsIntent(notifications));
            addIfNotNull(intents, DemoUtils.createClockIntent(clockHhmm, clockMillis));

            return new DemoModeRule(intents);
        }

        private static void addIfNotNull(@NonNull List<Intent> list, @Nullable Intent intent) {
            if (intent == null) {
                return;
            }

            list.add(intent);
        }
    }
}
