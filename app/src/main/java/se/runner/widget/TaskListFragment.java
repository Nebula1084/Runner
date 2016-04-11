package se.runner.widget;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import se.runner.R;


public class TaskListFragment extends Fragment implements PtrHandler{
    private ListView task_list;
    private BaseAdapter adapter;
    private AdapterView.OnItemClickListener onItemClickListener;
    private PtrFrameLayout task_list_ptr_frame;
    private int itemRsId;

    public TaskListFragment(){
        super();
    }

    public static TaskListFragment newInstance(int itemRsId){
        TaskListFragment instance = new TaskListFragment();
        instance.setItemRsId(itemRsId);
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_list, container, false);
        task_list = (ListView) view.findViewById(R.id.task_list);
        task_list_ptr_frame=(PtrFrameLayout) view.findViewById(R.id.task_list_ptr_frame);
        StoreHouseHeader header = new StoreHouseHeader(getContext());
        task_list_ptr_frame.setHeaderView(header);
        task_list_ptr_frame.setPtrHandler(this);
        setAdapter(new TaskListAdapter(getContext(), this.itemRsId));
        if (adapter != null)
            task_list.setAdapter(adapter);
        if (onItemClickListener != null)
            task_list.setOnItemClickListener(onItemClickListener);
        return view;
    }

    void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setItemRsId(int itemRsId){
        this.itemRsId=itemRsId;
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
        Toast.makeText(getContext(), "refresh", Toast.LENGTH_LONG).show();
        ptrFrameLayout.refreshComplete();
    }
}
