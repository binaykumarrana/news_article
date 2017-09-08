package binay.inshortapp;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import binay.inshortapp.model.Article;
import co.uk.rushorm.android.AndroidInitializeConfig;
import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCore;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by rajkiran on 08/09/17.
 */

public class InshortApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //Rushorm
        List<Class<? extends Rush>> classes = new ArrayList<>();
        classes.add(Article.class);
        AndroidInitializeConfig config = new AndroidInitializeConfig(getApplicationContext(), classes);
        RushCore.initialize(config);
        //Caligraphy for font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/raleway_semi_bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()

        );
    }
}
