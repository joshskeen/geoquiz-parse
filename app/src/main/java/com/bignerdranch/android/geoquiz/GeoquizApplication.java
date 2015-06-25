package com.bignerdranch.android.geoquiz;

import android.app.Application;

import com.bignerdranch.android.geoquiz.model.QuizQuestionManager;
import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseUser;

public class GeoquizApplication extends Application {

    //app id and key from Parse
    public static final String YOUR_APPLICATION_ID = "hyWlLYv5OOuTlCcggT1AXjMqniN1yIw3mISFs5YH";
    public static final String YOUR_CLIENT_KEY = "6b9sWvXP7V4ZWAWoveXcaNDHJoK74sbcPhBn7Kge";

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
