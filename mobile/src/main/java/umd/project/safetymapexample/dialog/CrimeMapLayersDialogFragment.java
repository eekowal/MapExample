package umd.project.safetymapexample.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ContextThemeWrapper;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import umd.project.safetymapexample.R;
import umd.project.safetymapexample.util.SharedPreferencesUtil;

public class CrimeMapLayersDialogFragment extends DialogFragment {

  public static final String TAG = CrimeMapLayersDialogFragment.class.getSimpleName();
  private static List<CharSequence>
      mLayers = Arrays.asList(new CharSequence[]{"Assault", "Theft"});

  @NonNull @Override public Dialog onCreateDialog(Bundle savedInstanceState) {
    final Set<String> mSelected = SharedPreferencesUtil.getSelectedLayers(this);
    boolean[] checked = convertToBooleans(mSelected);
    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.AlertDialogCustom2));
    builder.setTitle("Map Layers")
        .setMultiChoiceItems((CharSequence[]) mLayers.toArray(), checked, new DialogInterface.OnMultiChoiceClickListener() {
          @Override public void onClick(DialogInterface dialog, int which, boolean isChecked) {
             if(isChecked){
                mSelected.add(mLayers.get(which).toString());
             } else {
                mSelected.remove(mLayers.get(which).toString());
             }
          }
        })
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("crime_map", Context.MODE_PRIVATE);
            sharedPreferences.edit().putStringSet("layers", mSelected).apply();
          }
        })
        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
            CrimeMapLayersDialogFragment.this.getDialog().cancel();
          }
        });

    return builder.create();
  }

  private boolean[] convertToBooleans(Set<String> selectedStr){
    boolean[] selectedBool = new boolean[mLayers.size()];
    for (int i = 0; i < selectedBool.length; i++) {
      selectedBool[i] = false;
    }
    for(String string : selectedStr){
      selectedBool[mLayers.indexOf(string)] = true;
    }
    return selectedBool;
  }

}
