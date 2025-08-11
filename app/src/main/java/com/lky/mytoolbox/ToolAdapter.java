package com.lky.mytoolbox;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ToolAdapter extends RecyclerView.Adapter<ToolAdapter.ToolViewHolder> {

    private List<Tool> toolList;
    private Context context;

    public ToolAdapter(Context context, List<Tool> toolList) {
        this.context = context;
        this.toolList = toolList;
    }

    @NonNull
    @Override
    public ToolViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tool, parent, false);
        return new ToolViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ToolViewHolder holder, int position) {
        Tool tool = toolList.get(position);
        holder.toolName.setText(tool.getName());
        holder.toolIcon.setImageResource(tool.getIconResId());
        holder.itemView.setOnClickListener(v -> {
            try {
                context.startActivity(new Intent(context, tool.getActivityClass()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public int getItemCount() {
        return toolList.size();
    }

    static class ToolViewHolder extends RecyclerView.ViewHolder {
        ImageView toolIcon;
        TextView toolName;

        public ToolViewHolder(@NonNull View itemView) {
            super(itemView);
            toolIcon = itemView.findViewById(R.id.tool_icon);
            toolName = itemView.findViewById(R.id.tool_name);
        }
    }
}