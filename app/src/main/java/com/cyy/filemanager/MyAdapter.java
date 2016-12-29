package com.cyy.filemanager;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyy.filemanager.file.FileModel;

import java.util.List;

/**
 * Created by study on 16/12/21.
 *
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener,View.OnLongClickListener{

    private List<FileModel> items;
    private Context context;

    private OnItemClickListener clickListener;
    private OnItemLongClickListener longListener;

    MyAdapter(List<FileModel> items , Context context){
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_item , parent , false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        FileModel fileModel = items.get(position);
        holder.textView.setText(fileModel.name);

        if (fileModel.isDir){
            holder.iconView .setImageResource(R.drawable.ic_dir);
        }else {
            holder.iconView .setImageResource(R.drawable.ic_know);
        }

        ///可读可写可执行权限
        String permission = "";
        permission += fileModel.file.canRead() ? "r" :"-";
        permission += fileModel.file.canWrite() ? "w" :"-";
        permission += fileModel.file.canExecute() ? "x" :"-";
        holder.fileInfoView.setText(permission);

        ////选择的背景变化
        if (fileModel.select){
            ///选中
            holder.bgView.setBackgroundColor(Color.parseColor("#88bfbfbf"));
        }else {
            ///未选中
            holder.bgView.setBackgroundColor(Color.parseColor("#00bfbfbf"));
        }

        holder.rootView.setTag(position);
        holder.rootView.setOnClickListener(this);
        holder.rootView.setOnLongClickListener(this);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(View view) {
        if (clickListener!=null)clickListener.onItemClickListener(view , (Integer) view.getTag());
    }

    @Override
    public boolean onLongClick(View view) {
        if (longListener!=null)longListener.onItemLongClickListener(view , (Integer) view.getTag());
        return true;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        public View rootView;
        public TextView textView;
        public ImageView iconView;
        public TextView fileInfoView;
        public View bgView;

        public ViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            textView = (TextView) itemView.findViewById(R.id.name);
            iconView = (ImageView) itemView.findViewById(R.id.icon);
            fileInfoView = (TextView) itemView.findViewById(R.id.fileInfo);
            bgView = itemView.findViewById(R.id.bgView);
        }
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setLongListener(OnItemLongClickListener longListener) {
        this.longListener = longListener;
    }

    public interface OnItemClickListener{
        void onItemClickListener(View view , int position);
    }
    public interface OnItemLongClickListener{
        void onItemLongClickListener(View view , int position);
    }

}