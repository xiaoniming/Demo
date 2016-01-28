package com.seu.ni.demo.Media.Music;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.seu.ni.demo.R;

import java.io.File;

/**
 * Created by ni on 2015/11/19.
 */
public class ListControl {
    static final String MSG = "myMessage";
    File[] musicFiles;
    ListView listView;
    MusicFileAdapter musicFileAdapter;
    private int selectItem = -1;
    Context mContext;

    /**
     * @param listView
     * @param files
     */
    public ListControl(ListView listView, File[] files) {
        musicFiles = files;
        this.listView = listView;

    }

    /**
     * ListView 初始内容显示   *
     */
    public void initListView(Context context) {
        mContext=context;
        musicFileAdapter = new MusicFileAdapter(context);
        listView.setAdapter(musicFileAdapter);

//        ArrayList<String> arrayList = new ArrayList<>();
//        for (int i = 0; i < musicFiles.length; i++) {
//            arrayList.add(musicFiles[i].getName());
//        }
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout
//                .simple_expandable_list_item_1, arrayList);
//        listview.setAdapter(arrayAdapter);

    }


    /**
     * 传入position值，通知 Listview 刷新
     * @param position 更新值
     */

    public void setSelectItem(int position) {
        selectItem = position;
        musicFileAdapter.notifyDataSetChanged();
        listView.smoothScrollToPosition(selectItem);
    }


    /**
     *
     */

    public class MusicFileAdapter extends BaseAdapter {
        LayoutInflater inflater;


        public MusicFileAdapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return musicFiles.length;
        }

        @Override
        public Object getItem(int position) {
            return musicFiles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            Log.i(MSG, "" + position);

            convertView = inflater.inflate(R.layout.custom_singleline, null);
            TextView tv = (TextView) convertView.findViewById(R.id.linetext);
            tv.setText(musicFiles[position].getName());

            //正在播放的歌曲
            if (position == selectItem) {

//                convertView.setAlpha(0.5f);
//                convertView.setBackgroundColor(mContext.getResources().getColor(R.color.wheat));
                tv.setTextColor(mContext.getResources().getColor(R.color.orange));
            }
            return convertView;
        }
    }


}
