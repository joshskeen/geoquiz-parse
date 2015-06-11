### Geoquiz is back, with a snazzy [Parse](www.parse.com) backend for sharing quiz questions with world!

---
##setup
1. https://www.parse.com/signup - create an account
2. create a new app at https://www.parse.com/apps
3. visit the "Keys" section of the app you just created. Copy the Application ID & Client Key
4. Parse libraries need to be added next. Unfortunately they're not officially available in maven central, but we can work around that using [jitpack.io](https://jitpack.io/), a new competitor to maven central that makes it a snap to make libraries distributable.
5. `build.gradle` :

	```
		allprojects {
		    repositories {
		        jcenter()
		        maven {
		            url "https://jitpack.io"
		        }
		    }
		}

	```
	
6. `app/build.gradle` :

	```
	dependencies {
	    compile 'com.android.support:appcompat-v7:22.1.0'
	    //load dependency from jitpack
	    compile 'com.github.yongjhih:parse:1.9.2'
	    compile 'com.github.yongjhih:parse-crash-reporting:1.9.2'
	    compile 'com.github.yongjhih:parse-facebook-v3:1.9.2'
	    compile 'com.facebook.android:facebook-android-sdk:3.20.0'
	    compile 'com.parse.bolts:bolts-android:1.1.4'
	}
	```
		
4. Create a subclass of Application : 

	```
	
	public class GeoquizApplication extends Application {
	
	    //app id and key from Parse
	    public static final String YOUR_APPLICATION_ID = "your app ud";
	    public static final String YOUR_CLIENT_KEY = "your client key";
	
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
	
	
	```
	Note QuizQuestionManager - it's new class we'll create to manage interacting with our Parse-backed quiz questions. 
	
	
5. change your manifest to refer to the GeoquizApplication: 
 
	```
	    <application
        android:name=".GeoquizApplication"
        ...

	```
6. We'll build our question models as a subclass of ParseObject, which will imbue the object with special methods that let us write to the cloud.
`ParseQuizQuestion.java: `

```
@ParseClassName("ParseQuizQuestion")
public class ParseQuizQuestion extends ParseObject {
    public static final String MODEL_NAME = "ParseQuizQuestion";

    public ParseQuizQuestion() {
    }

    public String getQuestionText() {
        return getString(QUESTION_FIELD_KEY);
    }

    public void setQuestion(String question) {
        put(QUESTION_FIELD_KEY, question);
    }

    public boolean isAnswerTrue() {
        return getBoolean(ANSWER_IS_TRUE);
    }

    public void setIsAnswerTrue(boolean isTrue) {
        put(ANSWER_IS_TRUE, isTrue);
    }

    @Override
    public String toString() {
        return "ParseQuizQuestion{" + getQuestionText() + ", }";
    }
}

```

QuizQuestionManager will be responsible for creating and loading ParseQuizQuestion objects in the cloud.

`QuizQuestionManager.java: `

```
public class QuizQuestionManager {

    public static void registerParseObjects() {
        ParseObject.registerSubclass(ParseQuizQuestion.class);
    }

    public void addQuestion(String question, boolean isTrue, final QuizAddedCallback quizAddedCallback) {
        ParseQuizQuestion parseQuizQuestion = new ParseQuizQuestion();
        parseQuizQuestion.setQuestion(question);
        parseQuizQuestion.setIsAnswerTrue(isTrue);

        parseQuizQuestion.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                quizAddedCallback.onQuizAdded();
            }
        });
    }

    public void loadQuestions(final QuizLoadedCallback callback) {
        ParseQuery<ParseQuizQuestion> query = new ParseQuery<>(ParseQuizQuestion.MODEL_NAME);
        query.findInBackground(new FindCallback<ParseQuizQuestion>() {
            @Override
            public void done(List<ParseQuizQuestion> list, ParseException e) {
                callback.onQuestionsLoaded(list);
            }
        });
    }
}

```
Since the saveInBackground & findInBackground methods are asynchronous, we will use callbacks interfaces that point to the calling activity to find out when they complete. 

```
public interface QuizLoadedCallback {
    void onQuestionsLoaded(List<ParseQuizQuestion> questions);
}

public interface QuizAddedCallback {
    void onQuizAdded();
}

```

This is an overview of what's involved. You'll still need to wire up a new activity for creating a Quiz item and replace your QuestionBank with a list of `ParseQuizQuestions`.. 




