package com.cammuse.intelligence;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.cammuse.intelligence.tool.accelerator.ControlActivity;

public class ToolFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView gridView_toolFrag_functions;

    private String functionNames[];
    private int functionImgs[] = {R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,
            R.mipmap.ic_launcher, R.mipmap.ic_launcher, R.mipmap.ic_launcher,};
    private MyAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_tab_tool, container, false);
        initView(view);
        initList();
        initEvent();
        return view;
    }

    private void initList() {
        functionNames = ToolFragment.this.getActivity().getResources().getStringArray(R.array.tool_function_names);
        mAdapter = new MyAdapter();
        gridView_toolFrag_functions.setAdapter(mAdapter);
    }

    private void initView(View view) {
        gridView_toolFrag_functions = (GridView) view.findViewById(R.id.gridView_toolFrag_functions);

    }

    private void initEvent() {
        gridView_toolFrag_functions.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        switch (position) {
            case 0:
                intent = new Intent(ToolFragment.this.getActivity(), ControlActivity.class);
                startActivity(intent);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
        }
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return functionNames.length;
        }

        @Override
        public Object getItem(int position) {
            return functionNames[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder mViewHolder;
            if (convertView == null) {
                convertView = LayoutInflater.from(ToolFragment.this.getActivity()).inflate(R.layout.item_gridview_functions, null);
                mViewHolder = new ViewHolder();
                mViewHolder.imageView_functions_img = (ImageView) convertView.findViewById(R.id.imageView_functions_img);
                mViewHolder.textView_functions_name = (TextView) convertView.findViewById(R.id.textView_functions_name);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (ViewHolder) convertView.getTag();
            }
            mViewHolder.imageView_functions_img.setImageResource(functionImgs[position]);
            mViewHolder.textView_functions_name.setText(functionNames[position]);
            return convertView;
        }

        class ViewHolder {
            ImageView imageView_functions_img;
            TextView textView_functions_name;
        }
    }
}
