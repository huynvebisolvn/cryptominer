package com.heroku;

import java.io.FileInputStream;
import java.util.Calendar;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

public class FBInitialize {
	public FirebaseApp connet() {
		try {
            FileInputStream  refreshToken = new FileInputStream("src/main/resources/json/herokucoin.json");
            FirebaseOptions options  = new FirebaseOptions.Builder()
                        .setCredentials(GoogleCredentials.fromStream(refreshToken))
                        .setDatabaseUrl("https://herokucoin-58fe6-default-rtdb.firebaseio.com/")
                        .build();
            // lua chon path database
            String time = Calendar.getInstance().getTime().toString();
            return FirebaseApp.initializeApp(options,time);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}