package umd.project.safetymapexample.fragment;

import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.google.android.gms.maps.model.LatLng;
import umd.project.safetymapexample.R;

public class FeedFragment extends IDataViewFragment {

  private ListView mListView;
  private SimpleAdapter mAdapter;

  public FeedFragment(){}

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_feed, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mListView = (ListView) getView().findViewById(R.id.feed_list_view);
    mListView.setEmptyView(getEmptyView());
    // TODO: link to whatever the RSS data source is...
  }

  private View getEmptyView() {
    return getActivity().getLayoutInflater().inflate(R.layout.empty_rss_feed, (ViewGroup) getView(), true);
  }

  @Override public void updateLocation(LatLng location) {
    // TODO: pull RSS alerts for the location
  }
}
