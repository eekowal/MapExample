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

public class CrimeLayersDialogFragment extends DialogFragment {

    public static final String TAG = CrimeLayersDialogFragment.class.getSimpleName();

    public final static List<CharSequence> sLayers = Arrays.asList(
            new CharSequence[]{Type.ASSAULT, Type.THEFT, Type.CAR_THEFT, Type.SEX_OFFENSE, Type.ALL});

    public static class Type {
        public final static String ASSAULT = "Assault";
        public final static String THEFT = "Theft & Robbery";
        public final static String CAR_THEFT = "Car Theft";
        public final static String SEX_OFFENSE = "Sex Offense";
        public final static String ALL = "All";
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Set<String> mSelected = SharedPreferencesUtil.getSelectedCrimeMapLayers(this);

        int checked = getSelected(mSelected);
        AlertDialog.Builder builder =
                new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom));
        builder.setTitle("Crime Layers")
                .setIcon(R.drawable.ic_layers_white_48px)
                .setSingleChoiceItems((CharSequence[]) sLayers.toArray(), checked,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSelected.clear();
                                mSelected.add(sLayers.get(which).toString());
                            }
                        })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferencesUtil.saveSelectedCrimeMapLayers(getTargetFragment(), mSelected);
                        ((OnCrimeLayersDialogListener) getTargetFragment()).onLayersChanged(mSelected);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        CrimeLayersDialogFragment.this.getDialog().cancel();
                    }
                });

        return builder.create();
    }

    private int getSelected(Set<String> selectedStr) {
        Object[] selectedArray = (Object[]) selectedStr.toArray();
        String selected = (String) ((Object[]) selectedStr.toArray())[0];
        return sLayers.indexOf(selected);
    }

    public interface OnCrimeLayersDialogListener {
        void onLayersChanged(Set<String> selectedLayers);
    }
}
