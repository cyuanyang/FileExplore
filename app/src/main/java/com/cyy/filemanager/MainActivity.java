package com.cyy.filemanager;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.cyy.filemanager.file.Copy;
import com.cyy.filemanager.file.Delete;
import com.cyy.filemanager.file.FileManager;
import com.cyy.filemanager.file.FileModel;
import com.cyy.filemanager.file.dir.DirectorInfo;
import com.cyy.filemanager.views.MyFloatingActionsMenu;
import com.cyy.filemanager.views.bar.BarLayout;
import com.cyy.filemanager.views.dialog.dialog.AlertDialog;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MyAdapter.OnItemClickListener, MyAdapter.OnItemLongClickListener, BarLayout.MenuListener
, FloatingActionsMenu.OnFloatingActionsMenuUpdateListener , View.OnClickListener,Copy.CopyCallback{

    protected RecyclerView recycleView;
    protected BarLayout barLayout; //当前所在目录
    protected FloatingActionButton actionA;
    protected FloatingActionButton actionB;
    protected MyFloatingActionsMenu multipleActions;

    private MyAdapter adapter;
    private List<FileModel> datas = new ArrayList<>(10);

    private FileManager fileManager;
    private boolean isOperate = false;

//    private DirectorInfo.State state = new DirectorInfo.State();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();
//        MusicService musicService = new MusicService();

        fileManager = new FileManager(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycleView.setLayoutManager(linearLayoutManager);

        adapter = new MyAdapter(datas, this);
        recycleView.setAdapter(adapter);
        adapter.setClickListener(this);
        adapter.setLongListener(this);

        refreshFileByDir(fileManager.pushDir(fileManager.getCurrentDirFile()));
    }

    ///当前文件的信息改变是 调用更新一下
    private void refreshFileByDir(List<FileModel> fileModels) {
        datas.clear();
        datas.addAll(fileModels);

        adapter.notifyDataSetChanged();
        barLayout.setText(fileManager.getCurrentDirFile().getAbsolutePath());
    }

    private void initView() {
        recycleView = (RecyclerView) findViewById(R.id.recycleView);
        barLayout = (BarLayout) findViewById(R.id.barLayout);
        barLayout.setMenuListenert(this);
        actionA = (FloatingActionButton) findViewById(R.id.action_a);
        actionB = (FloatingActionButton) findViewById(R.id.action_b);
        multipleActions = (MyFloatingActionsMenu) findViewById(R.id.multiple_actions);
        multipleActions.setVisibility(View.GONE);
        multipleActions.setOnFloatingActionsMenuUpdateListener(this);

        actionA.setOnClickListener(this);
    }

    @Override
    public void onItemClickListener(View view, int position) {
        if (isOperate) {
            datas.get(position).select = !datas.get(position).select;
            if (datas.get(position).select) {
                view.findViewById(R.id.bgView).setBackgroundColor(Color.parseColor("#88bfbfbf"));
            } else {
                view.findViewById(R.id.bgView).setBackgroundColor(Color.parseColor("#00bfbfbf"));
            }
        } else {
            FileModel model = datas.get(position);
            if (model.isDir) {
                DirectorInfo.State state = new DirectorInfo.State();
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recycleView.getLayoutManager();
                state.position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                fileManager.saveState(state);
                Log.e("push =" , "push" +state.position);
                refreshFileByDir(fileManager.pushDir(model.file));

            } else {
                fileManager.openFile(model.file);
            }
        }
    }

    @Override
    public void onItemLongClickListener(View view, int position) {
//      ClipboardManager cmb = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

        isOperate = true;
        barLayout.isOperate(isOperate);

        datas.get(position).select = true;
        view.findViewById(R.id.bgView).setBackgroundColor(Color.parseColor("#88bfbfbf"));
    }

    @Override
    public void onBackPressed() {
        if (isOperate) {
            isOperate = false;
            barLayout.isOperate(isOperate);
            multipleActions.collapse();
            multipleActions.dissmiss();
            restoreUI();
            fileManager.cancleCopy();
        } else {
            if (TextUtils.equals(fileManager.getCurrentDirFile().getAbsolutePath(), "/")) {
                super.onBackPressed();
            } else {
                String parentDir = fileManager.getCurrentDirFile().getParent();
                if (!TextUtils.isEmpty(parentDir)) {
                    refreshFileByDir(fileManager.popDir());
                    ///回到记录的位置
                    DirectorInfo.State state  = fileManager.getCurrentState();
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recycleView.getLayoutManager();
                    linearLayoutManager.scrollToPositionWithOffset(state.position , 0);
                    Log.e("onBackPressed " , "po="+state.position);
                }
            }
        }
    }

    @Override
    public void copyAction() {
        fileManager.initCopy(datas , this , true);

        multipleActions.setVisibility(View.VISIBLE);
        multipleActions.expand();

        ///恢复UI
        isOperate = false;
        barLayout.isOperate(isOperate);
        restoreUI();
    }

    @Override
    public void cutAction() {
        fileManager.initCopy(datas , this , false);

        multipleActions.setVisibility(View.VISIBLE);
        multipleActions.expand();

        ///恢复UI
        isOperate = false;
        barLayout.isOperate(isOperate);
        restoreUI();
    }

    @Override
    public void deleteAction() {
        fileManager.deleteFile(datas, new Delete.Callback() {
            @Override
            public void success() {
                complete();
            }
        });
    }

    @Override
    public void moreAction() {

    }

    @Override
    public void onMenuExpanded() {

    }

    @Override
    public void onMenuCollapsed() {
        isOperate = false;
        barLayout.isOperate(isOperate);
        restoreUI();
        multipleActions.postDelayed(new Runnable() {
            @Override
            public void run() {
                multipleActions.dissmiss();
            }
        },300);
        fileManager.cancleCopy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.action_a){
            ///粘贴在此
            fileManager.paste(fileManager.getCurrentDirFile());
        }
    }

    @Override
    public void duplicate(final Copy copy ,final File desDir ,final FileModel sourceFile) {
        ///文件复制重复
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("目录下已经存在相同的文件，请选择").setLeftBtn("保留两者").setRightBtn("覆盖");
        builder.setLeftClickListener(new AlertDialog.OnLeftClickListener() {
            @Override
            public void onLeftClick(Dialog dialog, View v) {
                copy.keepSameDesDir(desDir , sourceFile);
            }
        });
        builder.setRightClickListener(new AlertDialog.OnRightClickListener() {
            @Override
            public void onRightClick(Dialog dialog, View v) {
                copy.coverCopy(desDir , sourceFile);
            }
        });
        builder.build().show();
    }

    @Override
    public void complete() {
        ///文件复制OK  刷新一下UI
        refreshFileByDir(fileManager.refreshCurrentDirectoryInfo());

        ///去掉 flating btn
        multipleActions.postDelayed(new Runnable() {
            @Override
            public void run() {
                multipleActions.dissmiss();
            }
        },300);
    }

    @Override
    public void dirPasteError(final Copy copy , final File desDir) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("文件夹不能复制到它的子文件夹，是否继续其他复制？").setLeftBtn("继续").setRightBtn("停止");
        builder.setLeftClickListener(new AlertDialog.OnLeftClickListener() {
            @Override
            public void onLeftClick(Dialog dialog, View v) {
                copy.paste(desDir);
            }
        });
        builder.setRightClickListener(new AlertDialog.OnRightClickListener() {
            @Override
            public void onRightClick(Dialog dialog, View v) {
                fileManager.cancleCopy();
                complete();
            }
        });
        builder.build().show();
    }

    ///恢复UI  取消选择的状态
    private void restoreUI() {
        for (FileModel fileModel : datas) {
            fileModel.select = fileModel.select ? false : false;
        }
        adapter.notifyDataSetChanged();
    }
}


