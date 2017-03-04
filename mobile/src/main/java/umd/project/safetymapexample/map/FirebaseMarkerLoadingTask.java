package umd.project.safetymapexample.map;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.google.android.gms.wearable.DataMap.TAG;


public class FirebaseMarkerLoadingTask extends AsyncTaskLoader {

    private final static String DATABASE_URL="https://safetymap-a90dc.firebaseio.com";

    public FirebaseMarkerLoadingTask(Context context) {
        super(context);
    }

    @Override
    public Object loadInBackground() {

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        Query query = databaseReference.child("safetymap-a90dc").child("data").endAt("1000");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot crimeSnapshot : dataSnapshot.getChildren()){
                    Log.i(TAG, "onDataChange: " + crimeSnapshot.getKey());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                databaseError.toException().printStackTrace();
            }
        });

        return query;
    }



}
