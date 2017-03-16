/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.emilyk.myapplication.backend;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.core.GeoHash;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseCredentials;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.emilyk.example.com",
                ownerName = "backend.myapplication.emilyk.example.com",
                packagePath = ""
        )
)

public class MyEndpoint {

    private static final String TAG = MyEndpoint.class.getSimpleName();

    private final static String DATABASE_URL = "https://safetymap-a90dc.firebaseio.com";

    private final static String GEOFIRE_URL = DATABASE_URL + "/_geofire";

    private final static String PATH_TO_ADMIN_ACCOUNT = "WEB-INF/safetymap-a90dc-firebase-adminsdk-v3pdm-9d18ece470.json";

    @ApiMethod(name = "setUpDb")
    public void setUpDatabase() {

        try {
            FileInputStream serviceAccount = new FileInputStream(PATH_TO_ADMIN_ACCOUNT);

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredential(FirebaseCredentials.fromCertificate(serviceAccount))
                    .setDatabaseUrl(DATABASE_URL)
                    .build();

            FirebaseApp.initializeApp(options);

            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

            final DatabaseReference geofireReference = FirebaseDatabase.getInstance().getReferenceFromUrl(GEOFIRE_URL);

            final GeoFire geoFire = new GeoFire(geofireReference);

            Query query = databaseReference.child("data");

            query.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(final DataSnapshot dataSnapshot) {

                    for (DataSnapshot crimeSnapshot : dataSnapshot.getChildren()) {
                        ArrayList<String> crime = (ArrayList<String>) crimeSnapshot.getValue();

                        double lat = Double.valueOf(crime.get(20));
                        double lng = Double.valueOf(crime.get(21));

                        geoFire.setLocation(crimeSnapshot.getKey(), new GeoLocation(lat, lng));

                        geofireReference.child(crimeSnapshot.getKey()).child("g").setValue(new GeoHash(lat, lng, 22).getGeoHashString());

                        databaseReference.child("geohash").child(crimeSnapshot.getKey()).setValue(new GeoHash(lat, lng, 22));

                        Log.i(TAG, "Crime " + crimeSnapshot.getKey() + ": " + crime);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    databaseError.toException().printStackTrace();
                }

            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}

