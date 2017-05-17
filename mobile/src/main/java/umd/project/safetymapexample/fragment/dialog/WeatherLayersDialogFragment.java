package umd.project.safetymapexample.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ContextThemeWrapper;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import umd.project.safetymapexample.R;
import umd.project.safetymapexample.util.SharedPreferencesUtil;

public class WeatherLayersDialogFragment extends DialogFragment {

    public static final String TAG = WeatherLayersDialogFragment.class.getSimpleName();

    public final static List<CharSequence> sLayers = Arrays.asList(
            new CharSequence[]{Type.RAIN, Type.TEMP, Type.WINDS});

    public static class Type {
        public final static String RAIN = "Rain";
        public final static String TEMP = "Temperature";
        public final static String WINDS = "Winds";
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Set<String> mSelected = SharedPreferencesUtil.getSelectedWeatherMapLayers(this);
        boolean[] checked = getSelected(mSelected);

        AlertDialog.Builder builder =
                new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        builder.setTitle("Weather Layers")
                .setIcon(R.drawable.ic_layers_white_48px)
                .setMultiChoiceItems((CharSequence[]) sLayers.toArray(), checked,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if (isChecked) {
                                    mSelected.add(sLayers.get(which).toString());
                                } else {
                                    mSelected.remove(sLayers.get(which).toString());
                                }
                            }
                        }
                ).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                SharedPreferencesUtil.saveSelectedWeatherMapLayers(getTargetFragment(), mSelected);
                ((OnMapLayersDialogListener) getTargetFragment()).onLayersChanged(mSelected);
            }
        })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        WeatherLayersDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private boolean[] getSelected(Set<String> selectedStr) {
        Object[] selectedArray = (Object[]) selectedStr.toArray();
        if (selectedArray.length == 0) {
            return new boolean[]{true, false, false};
        } else {
            boolean[] selected = new boolean[sLayers.size()];
            for (int i = 0; i < selected.length; i++) {
                selected[i] = false;
            }
            for (String s : selectedStr) {
                selected[sLayers.indexOf(s)] = true;
            }
            return selected;
        }

    }

    public interface OnMapLayersDialogListener {
        void onLayersChanged(Set<String> selectedLayers);
    }
}
