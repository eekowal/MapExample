package umd.project.safetymapexample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent data = getSettings();
                setResult(RESULT_OK, data);
                finish();

            }
        });

        ImageView closeButton = (ImageView) findViewById(R.id.close);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

    }

    private Intent getSettings(){
        RadioGroup displayRadioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        int selectedDisplay = displayRadioGroup.getCheckedRadioButtonId();

        RadioGroup locationRadioGroup = (RadioGroup) findViewById(R.id.location_radiogroup);
        int selectedLocation = locationRadioGroup.getCheckedRadioButtonId();

        String location = ((RadioButton) findViewById(selectedLocation)).getText().toString();
        double[] coords = getLongitudeAndLatitudeFromString(location);

        Intent data = new Intent();
        data.putExtra("LOCATION", coords);
        data.putExtra("DISPLAY_TYPE", selectedDisplay);

        return data;
    }

    private double[] getLongitudeAndLatitudeFromString(String location){
        String[] coords = location.split("\\s*,\\s*");
        return new double[]{Double.valueOf(coords[0]), Double.valueOf(coords[1])};
    }

}
