package com.cyy.filemanager;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cyy.filemanager.file.Copy;
import com.cyy.filemanager.file.Delete;
import com.cyy.filemanager.file.FileManager;
import com.cyy.filemanager.file.FileModel;
import com.cyy.filemanager.file.FileType;
import com.cyy.filemanager.file.SortFile;
import com.cyy.filemanager.file.dir.DirectorInfo;
import com.cyy.filemanager.tools.Persistence;
import com.cyy.filemanager.views.MyFloatingActionsMenu;
import com.cyy.filemanager.views.bar.BarLayout;
import com.cyy.filemanager.views.dialog.AlertDialog;
import com.cyy.filemanager.views.dialog.ChooseDialog;
import com.cyy.filemanager.views.menu.MenuLayout;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        MyAdapter.OnItemClickListener, MyAdapter.OnItemLongClickListener, BarLayout.MenuListener
        , FloatingActionsMenu.OnFloatingActionsMenuUpdateListener, View.OnClickListener,
        Copy.CopyCallback,MenuLayout.OnMenuCallback , ChooseDialog.Callback {

    private final static int requestFilePremissionCode = 100;
    protected RecyclerView recycleView;
    protected BarLayout barLayout; //当前所在目录
    protected FloatingActionButton actionA;
    protected FloatingActionButton actionB;
    protected MyFloatingActionsMenu multipleActions;
    protected DrawerLayout drawerLayout;
    protected TextView requestPermission;
    protected MenuLayout menuLayout;

    private MyAdapter adapter;
    private List<FileModel> datas = new ArrayList<>(10);

    private FileManager fileManager;
    private boolean isOperate = false;///处于进入操作的状态
    private boolean isPaste = false; //处于复制后等待粘贴的状态

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == requestFilePremissionCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestPermission.setVisibility(View.GONE);
                recycleView.setVisibility(View.VISIBLE);
                refreshFileByDir(fileManager.pushDir(fileManager.getCurrentDirFile()));
            } else {
                recycleView.setVisibility(View.GONE);
                requestPermission.setVisibility(View.VISIBLE);
            }
        }
    }

    private void requestStorePermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE")
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"},
                    requestFilePremissionCode);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        requestStorePermission();
        init();
    }

    private void init() {
        fileManager = new FileManager(this);
        initView();
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
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        multipleActions.setVisibility(View.GONE);
        multipleActions.setOnFloatingActionsMenuUpdateListener(this);
        requestPermission = (TextView) findViewById(R.id.requestPermission);
        requestPermission.setOnClickListener(MainActivity.this);
        requestPermission.setVisibility(View.GONE);

        actionA.setOnClickListener(this);
        menuLayout = (MenuLayout) findViewById(R.id.menu_layout);
        menuLayout.setMenuCallback(this);
        menuLayout.setMenuSortCallback(this);
        menuLayout.setShowHideFile(Persistence.getBoolean(Persistence.kHideFile , false));
        fileManager.isShowHideFile(Persistence.getBoolean(Persistence.kHideFile , false));
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
                Log.e("push =", "push" + state.position);
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
        } else if (isPaste) {
            ///处于等待粘贴的状态
            fileManager.cancleCopy();
            ///去掉 flating btn
            multipleActions.collapse();
            multipleActions.postDelayed(new Runnable() {
                @Override
                public void run() {
                    multipleActions.dissmiss();
                }
            }, 300);
            isPaste = false;
        } else {
            if (TextUtils.equals(fileManager.getCurrentDirFile().getAbsolutePath(), FileManager.ROOT_DIR.getAbsolutePath())) {
                super.onBackPressed();
            } else {
                String parentDir = fileManager.getCurrentDirFile().getParent();
                if (!TextUtils.isEmpty(parentDir)) {
                    refreshFileByDir(fileManager.popDir());
                    ///回到记录的位置
                    DirectorInfo.State state = fileManager.getCurrentState();
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recycleView.getLayoutManager();
                    linearLayoutManager.scrollToPositionWithOffset(state.position, 0);
                    Log.e("onBackPressed ", "po=" + state.position);
                }
            }
        }
    }

    @Override
    public void menuAction() {
        drawerLayout.openDrawer(Gravity.LEFT);
    }

    @Override
    public void copyAction() {
        fileManager.initCopy(datas, this, true);
        isPaste = true;
        multipleActions.setVisibility(View.VISIBLE);
        multipleActions.expand();

        ///恢复UI
        isOperate = false;
        barLayout.isOperate(isOperate);
        restoreUI();
    }

    @Override
    public void cutAction() {
        fileManager.initCopy(datas, this, false);
        isPaste = true;
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
        isPaste = false;
        barLayout.isOperate(isOperate);
        restoreUI();
        multipleActions.postDelayed(new Runnable() {
            @Override
            public void run() {
                multipleActions.dissmiss();
            }
        }, 300);
        fileManager.cancleCopy();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.action_a) {
            ///粘贴在此
            fileManager.paste(fileManager.getCurrentDirFile());
        } else if (v.getId() == R.id.requestPermission) {
            requestStorePermission();
        }
    }

    @Override
    public void duplicate(final Copy copy, final File desDir, final FileModel sourceFile) {
        ///文件复制重复
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("目录下已经存在相同的文件，请选择").setLeftBtn("保留两者").setRightBtn("覆盖");
        builder.setLeftClickListener(new AlertDialog.OnLeftClickListener() {
            @Override
            public void onLeftClick(Dialog dialog, View v) {
                copy.keepSameDesDir(desDir, sourceFile);
            }
        });
        builder.setRightClickListener(new AlertDialog.OnRightClickListener() {
            @Override
            public void onRightClick(Dialog dialog, View v) {
                copy.coverCopy(desDir, sourceFile);
            }
        });
        builder.setOnCancelClickListener(new AlertDialog.OnCancelClickListener() {
            @Override
            public void onCancelClick(Dialog dialog, View v) {
                fileManager.cancleCopy();
                complete();
            }
        });
        builder.build().show();
    }

    @Override
    public void complete() {
        ///文件复制OK  刷新一下UI
        refreshFileByDir(fileManager.refreshCurrentDirectoryInfo());

        multipleActions.toggle();
        ///去掉 flating btn
        multipleActions.postDelayed(new Runnable() {
            @Override
            public void run() {
                multipleActions.dissmiss();
            }
        }, 300);
    }

    @Override
    public void dirPasteError(final Copy copy, final File desDir) {
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

    /********* 菜单事件 *************/
    @Override
    public void onMenuChangeHideFileState(boolean isShow) {
        Log.d("main>>" , "show hide file ＝ "+ isShow);
        fileManager.isShowHideFile(isShow);
        Persistence.insertBoolean(Persistence.kHideFile , isShow);
        ///刷刷新目录
        refreshFileByDir(fileManager.refreshCurrentDirectoryInfo());
        drawerLayout.closeDrawers();
    }

    /********* 菜单排序事件 *************/
    @Override
    public void sortByName() {
        if (fileManager.getSortType()!=SortFile.SORT_BY_NAME){
            fileManager.sortFileModel(datas , SortFile.SORT_BY_NAME);
            adapter.notifyDataSetChanged();
        }
        drawerLayout.closeDrawers();
    }

    @Override
    public void sortByTime() {

    }

    @Override
    public void sortByType() {
        if (fileManager.getSortType()!=SortFile.SORT_BY_TYPE){
            fileManager.sortFileModel(datas , SortFile.SORT_BY_TYPE);
            adapter.notifyDataSetChanged();
        }
        drawerLayout.closeDrawers();
    }

}


