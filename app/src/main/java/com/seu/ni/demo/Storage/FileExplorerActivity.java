package com.seu.ni.demo.Storage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.seu.ni.demo.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileExplorerActivity extends Activity {
    static final String MSG = "------FileExplorer";
    Button bt_back;
    TextView tv_path;
    ListView lv_files;
    File currentParent;
    File[] currentFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_explorer);
        initView();


        /**获取系统根目录**/
        String sdPath = Environment.getExternalStorageDirectory().getPath();
        Log.i(MSG, sdPath);
        final File root = new File(sdPath);
        if (root.exists()) {
            currentParent = root;
            currentFiles = currentParent.listFiles();
            inflateListView(currentFiles);
        }
        /**设置返回键监听**/
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!currentParent.getCanonicalPath().equals(root.getCanonicalPath())) {
                        currentParent = currentParent.getParentFile();
                        Log.i(MSG, currentParent.getAbsolutePath());
                        currentFiles = currentParent.listFiles();
                    } else {
                        Toast.makeText(FileExplorerActivity.this, "已经到根目录", Toast.LENGTH_SHORT).show();

                    }
                    inflateListView(currentFiles);
                } catch (IOException e) {

                }
            }
        });
        /**设置ListView键监听**/
        lv_files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (currentFiles[position].isFile()) return;
                currentParent = currentFiles[position];
                Log.i(MSG, currentParent.getAbsolutePath());
                currentFiles = currentParent.listFiles();
                inflateListView(currentFiles);
                if (currentFiles.length == 0) {
                    Toast.makeText(FileExplorerActivity.this, "文件夹为空", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void initView() {
        /**获取组件**/
        bt_back = (Button) findViewById(R.id.bt_file_back);
        tv_path = (TextView) findViewById(R.id.tv_file_path);
        lv_files = (ListView) findViewById(R.id.lv_files);

    }

    /**
     * ListView 填充
     *
     * @param files
     */

    void inflateListView(File[] files) {
        //创建一个 List 集合，集合元素是 Map
        List<Map<String, Object>> ListItems = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            // 用 HashMap 来实例化 item
            Map<String, Object> item = new HashMap<>();
            // 图标的选用

            if (files[i].isDirectory()) {
                item.put("icon", R.drawable.file_dir);
            } else {
                item.put("icon", R.drawable.file_file);
            }
            item.put("fileName", files[i].getName());

            //添加 List 项
            ListItems.add(item);

        }

        // 创建一个 SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, ListItems, R.layout.custom_file_singleline, new
                String[]{"icon", "fileName"}, new int[]{R.id.icon, R.id.file_name});
        // 设置 Adapter
        lv_files.setAdapter(simpleAdapter);
        try {
            tv_path.setText("当前路径：" + currentParent.getCanonicalPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
