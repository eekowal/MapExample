package umd.project.safetymapexample.tiles;

import com.google.android.gms.maps.model.TileOverlay;

import java.util.HashMap;
import java.util.Map;

public class TileOverlayManager {

    private Map<String, TileOverlay> mTileOverlays;

    public TileOverlayManager() {
        mTileOverlays = new HashMap<>();
    }

    public void add(TileOverlay tileOverlay) {
        mTileOverlays.put(tileOverlay.getId(), tileOverlay);
    }

    public void remove(TileOverlay tileOverlay) {
        mTileOverlays.remove(tileOverlay.getId());
    }

    public void clear() {
        mTileOverlays.clear();
    }
}
