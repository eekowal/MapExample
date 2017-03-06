package umd.project.safetymapexample.map;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;

import static com.google.android.gms.wearable.DataMap.TAG;


public class FirebaseMarkerLoadingTask extends AsyncTaskLoader {

    private DataSnapshot mDataSnapshot;

    private final static String DATABASE_URL="https://safetymap-a90dc.firebaseio.com";

    public FirebaseMarkerLoadingTask(Context context, DataSnapshot dataSnapshot) {
        super(context);
        mDataSnapshot = dataSnapshot;
    }

    @Override
    public Object loadInBackground() {

        Log.i(TAG, "loadInBackground: ");

        ArrayList<LatLng> list = new ArrayList<>();

        for(DataSnapshot crimeSnapshot : mDataSnapshot.getChildren()){
            ArrayList<String> crime = (ArrayList<String>) crimeSnapshot.getValue();

            double lat = Double.valueOf((String) crime.get(20));
            double lng = Double.valueOf((String) crime.get(21));

            list.add(new LatLng(lat, lng));

            Log.i(TAG, "Crime " + crimeSnapshot.getKey() + ": " + crime);
        }

        return list;
    }



}
