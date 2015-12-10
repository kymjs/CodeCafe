package top.codecafe.fragment;

import android.view.View;

import com.kymjs.frame.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.model.osc.Favorite;

import java.util.ArrayList;

/**
 * @author kymjs (http://www.kymjs.com/) on 12/10/15.
 */
public class TopListFragment extends MainListFragment<Favorite> {
    @Override
    protected BasePullUpRecyclerAdapter<Favorite> getAdapter() {
        return null;
    }

    @Override
    protected ArrayList<Favorite> parserInAsync(byte[] t) {
        return null;
    }

    @Override
    public void doRequest() {

    }

    @Override
    public void onItemClick(View view, Object data, int position) {

    }
}
