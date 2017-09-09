package binay.inshortapp;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import binay.inshortapp.model.Article;
import binay.inshortapp.model.Publisher;
import co.uk.rushorm.android.AndroidInitializeConfig;
import co.uk.rushorm.core.Rush;
import co.uk.rushorm.core.RushCore;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by rajkiran on 08/09/17.
 */

public class InshortApplication extends Application {
    private boolean isInternetDialogVisible = false;
    public static InshortApplication sInshortApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        sInshortApplication = this;
        //Rushorm
        List<Class<? extends Rush>> classes = new ArrayList<>();
        classes.add(Article.class);
        classes.add(Publisher.class);
        AndroidInitializeConfig config = new AndroidInitializeConfig(getApplicationContext(), classes);
        RushCore.initialize(config);
        //Caligraphy for font
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/raleway_semi_bold.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()

        );
    }

    /*
    * Function to return main application instance*/
    public static InshortApplication getInstance() {
        return sInshortApplication;
    }

    /*Methods for internet availability check dialog*/
    public void setNoInternetDialogVisibility(boolean isVisible) {
        this.isInternetDialogVisible = isVisible;
    }


    public boolean getNoInternetDialogVisibility() {
        return isInternetDialogVisible;
    }
}
