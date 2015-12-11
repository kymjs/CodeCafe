package top.codecafe.fragment;

import android.view.View;

import com.kymjs.frame.adapter.BasePullUpRecyclerAdapter;
import com.kymjs.model.osc.OSCBlog;

import java.util.ArrayList;

/**
 * @author kymjs (http://www.kymjs.com/) on 12/11/15.
 */
public class OSCNewsListFragment extends MainListFragment<OSCBlog> {
    @Override
    protected BasePullUpRecyclerAdapter<OSCBlog> getAdapter() {
        return null;
    }

    @Override
    protected ArrayList<OSCBlog> parserInAsync(byte[] t) {
        return null;
    }

    @Override
    public void doRequest() {

    }

    @Override
    public void onItemClick(View view, Object data, int position) {

    }
}
