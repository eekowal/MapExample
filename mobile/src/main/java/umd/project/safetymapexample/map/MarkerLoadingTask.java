package umd.project.safetymapexample.map;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import umd.project.safetymapexample.R;


public class MarkerLoadingTask extends AsyncTaskLoader<List<LatLng>> {


    public MarkerLoadingTask(Context context){
        super(context);
    }

    @Override
    public List<LatLng> loadInBackground() {
        ArrayList<LatLng> list = new ArrayList<LatLng>();
        InputStream inputStream = getContext().getResources().openRawResource(R.raw.prince_georges_small);
        String json = new Scanner(inputStream).useDelimiter("\\A").next();
        try {
            JSONObject object = new JSONObject(json);
            JSONArray array = object.getJSONArray("data");
            for (int i = 0; i < array.length(); i++) {
                JSONArray a = (JSONArray) array.get(i);
                LatLng latLng = getLongitudeAndLatitude(a);
                list.add(latLng);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    private LatLng getLongitudeAndLatitude(JSONArray jsonArray) {
        double lat = 0;
        double lng = 0;

        try {
            lat = Double.valueOf((String) jsonArray.get(20));
            lng = Double.valueOf((String) jsonArray.get(21));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return new LatLng(lat, lng);
    }

//    private interface MarkerQuery {
//
//        String[] PROJECTION = {
//
//        };
//
//        int MARKER_ID = 0;
//
//    }
//
//    public class MarkerEntry {
//
//        public MarkerModel model;
//        public MarkerOptions options;
//
//        public MarkerEntry(MarkerModel model, MarkerOptions options) {
//            this.model = model;
//            this.options = options;
//        }
//    }
}
