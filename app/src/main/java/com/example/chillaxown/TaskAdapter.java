package com.example.chillaxown;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> implements Filterable {

    ArrayList<TaskDetails> tasks;
    ArrayList<TaskDetails> filteredTasks;
    private static RecycleViewClickListener itemListener;
    private Context context;
    ValueFilter valueFilter;

    public TaskAdapter(ArrayList<TaskDetails> tasks) {
        this.tasks = tasks;
    }

    public TaskAdapter(ArrayList<TaskDetails> tasks, Context context, RecycleViewClickListener itemListener) {
        this.tasks = tasks;
        this.context = context;
        this.itemListener = itemListener;
        this.filteredTasks = tasks;
    }

    @Override
    public Filter getFilter() {
        if (valueFilter == null) {
            valueFilter = new ValueFilter();
        }
        return valueFilter;
    }

    /**
     * Class that implements Filterable to filter ActivityDetails
     */
    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            Log.i("filter",constraint+"");
            if (constraint != null && constraint.length() > 0) {
                ArrayList<TaskDetails> filterList = new ArrayList<TaskDetails>();
                for (int i = 0; i < filteredTasks.size(); i++) {
                    //filter the list based on the category, name and reporterName
                    if ( (filteredTasks.get(i).getTaskCategory().toUpperCase()
                            .contains(constraint.toString().trim().toUpperCase())) ||
                            (filteredTasks.get(i).getTaskName().toUpperCase()
                                    .contains(constraint.toString().trim().toUpperCase())) ||
                            (filteredTasks.get(i).getTaskDate().toUpperCase()
                                    .contains(constraint.toString().trim().toUpperCase())) ||
                            (filteredTasks.get(i).getIsComelpted().toUpperCase()
                                    .contains(constraint.toString().trim().toUpperCase()))
                    ) {
                        filterList.add(filteredTasks.get(i));
                    }
                }
                results.count = filterList.size();
                results.values = filterList;
            } else {
                results.count = filteredTasks.size();
                results.values = filteredTasks;
            }
            return results;
        } //end of performFiltering

        /**
         * Publishes the new ArrayList filtered and set it back to the
         * CustomListAdapter
         * @param constraint
         * @param results
         */
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            tasks = (ArrayList<TaskDetails>) results.values;
            notifyDataSetChanged();
        }  //end of publishResults
    } //end of innerClass

    public interface RecycleViewClickListener {
         void recycleViewListClicked(View v, int position);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new TaskViewHolder(row);

    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {
        HashMap<String, Integer> categoryDict = new HashMap<String, Integer>();
        /**
        categoryDict.put("Trip", Color.BLUE);
        categoryDict.put("Study", Color.GRAY);
        categoryDict.put("Chores", Color.GREEN);
        categoryDict.put("Family", Color.YELLOW);
        categoryDict.put("Relaxation", Color.MAGENTA);
        categoryDict.put("Urgent", Color.RED);**/
        taskViewHolder.tvTaskName.setText(tasks.get(i).getTaskName());
        taskViewHolder.tvTaskCategory.setText(tasks.get(i).getTaskCategory());
        taskViewHolder.tvTaskDate.setText(tasks.get(i).getTaskDate());
        taskViewHolder.tvTaskTime.setText(tasks.get(i).getTaskTime());
        //taskViewHolder.cv.setCardBackgroundColor(categoryDict.get(tasks.get(i).getTaskCategory()));

        //taskViewHolder.cv.setCardBackgroundColor(categoryDict.get(tasks.get(i).getTaskCategory()));
        HashMap<String, Bitmap> image = new HashMap<String, Bitmap>();

        if(tasks.get(i).getTaskCategory().equals("Trip")) {
            taskViewHolder.img.setImageResource(R.drawable.navy);
        }
        else if(tasks.get(i).getTaskCategory().equals("Study")){
            taskViewHolder.img.setImageResource(R.drawable.burgundary);
        }
        else if(tasks.get(i).getTaskCategory().equals("Chores")){
            taskViewHolder.img.setImageResource(R.drawable.light_green);
        }
        else if(tasks.get(i).getTaskCategory().equals("Family")){
            taskViewHolder.img.setImageResource(R.drawable.green);
        }
        else if(tasks.get(i).getTaskCategory().equals("Relaxation")){
            taskViewHolder.img.setImageResource(R.drawable.bar_light);
        }
        else if(tasks.get(i).getTaskCategory().equals("Urgent")){
            taskViewHolder.img.setImageResource((R.drawable.coral_pink));
        }
    }

    @Override
    public int getItemCount() {
        return (tasks == null) ? 0 : tasks.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void clear() {
        if(tasks != null)
            this.tasks.clear();
    }

    public void addAll(ArrayList<TaskDetails> tasks) {
        Collections.sort(tasks);
        this.tasks.addAll(tasks);
    }

    public TaskDetails getItem(int position) {
        return this.tasks.get(position);
    }

    /**
    public void filter(String category) {
        if (!category.equals("")) {
            ArrayList<TaskDetails> filteredTasks = new ArrayList<TaskDetails>();
            Log.i("ADAPTER", tasks.toString());
            for (TaskDetails t : tasks) {
                if (t.getIsComelpted().equals(category))
                    filteredTasks.add(t);
            }
            Collections.sort(filteredTasks);
            this.tasks.clear();
            this.tasks.addAll(filteredTasks);
        }
    }
     **/

    public static class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnCreateContextMenuListener, View.OnClickListener{
        CardView cv;
        TextView tvTaskName;
        TextView tvTaskDate;
        TextView tvTaskTime;
        TextView tvTaskCategory;
        ImageView img;

        TaskViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            tvTaskName = itemView.findViewById(R.id.row_task_name);
            tvTaskDate = itemView.findViewById(R.id.row_task_date);
            tvTaskTime = itemView.findViewById(R.id.row_task_time);
            tvTaskCategory = itemView.findViewById(R.id.row_task_category);
            img= (ImageView) itemView.findViewById(R.id.image);

            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(0, view.getId(), getAdapterPosition(), "Edit");
            contextMenu.add(0, view.getId(), getAdapterPosition(), "Delete");
            contextMenu.add(0, view.getId(), getAdapterPosition(), "Start Timer");
        }

        @Override
        public boolean onLongClick(View view) {
            return false;
        }

        @Override
        public void onClick(View view) {
            itemListener.recycleViewListClicked(view, this.getLayoutPosition());
        }

        /**
        @Override
        public void onClick(View view) {
            /*
            //Toast.makeText(view.getContext(),"Test",Toast.LENGTH_LONG).show();
            //Log.d("DEBUGTHIS","on click runs");
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemClick(position);
                }

            }
            */
    }

        /**
        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(view.getContext(),"Test",Toast.LENGTH_LONG).show();
            Log.d("DEBUGTHIS","on click runs");
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemLongClick(position);
                }

            }
            return true;
        }
        **/
    }

    /*@Override
    public boolean onLongClick(View view) {
    return false;
    }



*/
    /**
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onShowItemClick(int position);
        void onDeleteItemClick(int position);
        void onItemLongClick(int position);

    }

    //public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }
    **/

