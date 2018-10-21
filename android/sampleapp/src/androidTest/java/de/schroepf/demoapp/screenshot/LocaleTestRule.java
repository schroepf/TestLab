package de.schroepf.demoapp.screenshot;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LocaleTestRule<T extends Activity> extends ActivityTestRule<T> {
    private static Logger logger = Logger.getGlobal();


    private final List<Locale> locales;

    public LocaleTestRule(Class<T> activityClass, String... languageTags) {
        super(activityClass);

        List<Locale> locales = new ArrayList<>();

        for (String languageTag : languageTags) {
            locales.add(Locale.forLanguageTag(languageTag));
        }

        this.locales = locales;
    }

    @Override
    protected void beforeActivityLaunched() {
        super.beforeActivityLaunched();
        Log.wtf("ZEFIX", "beforeActivityLaunched: defaultLocale: " + Locale.getDefault());
    }

    @Override
    public Statement apply(final Statement base, final Description description) {
        if (locales.size() == 0) {
            return super.apply(base, description);
        }

        LocaleStatement localeStatement = new LocaleStatement();

        for (Locale locale : locales) {
            localeStatement.add(locale, super.apply(base, description));
        }

        return localeStatement;
    }

    @Nullable
    public Locale getCurrentLocale() {
        return Locale.getDefault();
    }

    private static class LocaleStatement extends Statement {

        private final Map<Locale, Statement> localeStatementMap = new HashMap<>();

        private Locale deviceLocale;

        LocaleStatement() {
        }

        @Override
        public void evaluate() throws Throwable {
            try {
                if (localeStatementMap.size() > 0) {
                    deviceLocale = Locale.getDefault();
                }

                for (Map.Entry<Locale, Statement> entry : localeStatementMap.entrySet()) {
                    logger.log(Level.INFO, "Changing locale to: " + entry.getKey());
                    setLocale(entry.getKey());
                    entry.getValue().evaluate();
                }
            } finally {
                if (deviceLocale != null) {
                    logger.log(Level.INFO, "Resetting locale to: " + deviceLocale);
                    setLocale(deviceLocale);
                }
            }
        }

        void add(Locale locale, Statement statement) {
            localeStatementMap.put(locale, statement);
        }

        static void setLocale(Locale locale) {
            Context context = InstrumentationRegistry.getTargetContext().getApplicationContext();

            Resources resources = context.getResources();
            Locale.setDefault(locale);
            Configuration config = resources.getConfiguration();

            if (Build.VERSION.SDK_INT >= 17) {
                config.setLocale(locale);
            } else {
                config.locale = locale;
            }

            context.createConfigurationContext(config);
            resources.updateConfiguration(config, resources.getDisplayMetrics());
        }
    }
}
