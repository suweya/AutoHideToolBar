package net.suweya.autohidetoolbar;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import net.suweya.autohidetoolbarhelper.AutoHideToolbarHelper;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends ListFragment {

    private static final int ITEM_COUNT = 100;

    private static String[] DATAS = new String[ITEM_COUNT];

    static {
        for (int i = 0; i < ITEM_COUNT; i++) {
            DATAS[i] = "Item" + String.valueOf(i);
        }
    }

    public MainActivityFragment() {
    }

    /*@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }*/

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1, DATAS);
        setListAdapter(adapter);
    }

    public void registerAutoHideListener(Toolbar toolbar) {
        Log.d("AutoHide", "register view");
        new AutoHideToolbarHelper(getActivity(), toolbar).registerOnScrollListener(getListView());
    }
}
