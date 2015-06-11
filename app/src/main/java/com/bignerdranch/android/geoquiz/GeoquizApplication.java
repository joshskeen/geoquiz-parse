package com.bignerdranch.android.geoquiz;

import android.app.Application;

import com.bignerdranch.android.geoquiz.model.QuizQuestionManager;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseUser;

public class GeoquizApplication extends Application {

    //app id and key from Parse
    public static final String YOUR_APPLICATION_ID = "pTX5u2MuOMIkMoiGy4NzABiIlm99uA8cAp10HH64";
    public static final String YOUR_CLIENT_KEY = "RdwPe8ijB2RdsCJcorv5RgoW2FhAG7gEv3puatGz";

    @Override
    public void onCreate() {
        super.onCreate();
        ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        ParseUser.enableAutomaticUser();
        QuizQuestionManager.registerParseObjects();
    }
}
