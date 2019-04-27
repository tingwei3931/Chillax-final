package com.example.chillaxown;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

//import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
//import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;


public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> /*implements DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener*/ {

    Context mContext;
    List<Post> mData;
    OnItemClickListener mListener;
    private static PostAdapter.RecycleViewClickListener itemListener;


    public PostAdapter(Context mContext, List<Post> mData, PostAdapter.RecycleViewClickListener itemListener) {
        this.mContext = mContext;
        this.mData = mData;
        this.itemListener = itemListener;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(mContext).inflate(R.layout.row_task_item,parent, false);

        return new MyViewHolder(row);

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //holder.tvTitle.setText(mData.get(position).getTitle());
        HashMap<String, Integer> categoryDict = new HashMap<String, Integer>();
        categoryDict.put("Trip", Color.BLUE);
        categoryDict.put("Study", Color.GRAY);
        categoryDict.put("Chores", Color.GREEN);
        categoryDict.put("Family", Color.YELLOW);
        categoryDict.put("Relaxation", Color.MAGENTA);
        categoryDict.put("Urgent", Color.RED);
        holder.tvTaskName.setText(mData.get(position).getTaskName());
        holder.tvTaskCategory.setText(mData.get(position).getTaskCategory());
        holder.tvTaskDate.setText(mData.get(position).getTaskDate());
        holder.tvTaskTime.setText(mData.get(position).gettime());
        holder.cv.setCardBackgroundColor(categoryDict.get(mData.get(position).getTaskCategory()));


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public interface RecycleViewClickListener {
        void recycleViewListClicked(View v, int position);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }







    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        CardView cv;
        TextView tvTaskName;
        TextView tvTaskDate;
        TextView tvTaskTime;
        TextView tvTaskCategory;

        MyViewHolder(View itemView) {
            super(itemView);

            cv = (CardView) itemView.findViewById(R.id.cv);
            tvTaskName = itemView.findViewById(R.id.task_name);
            tvTaskDate = itemView.findViewById(R.id.task_date);
            tvTaskTime = itemView.findViewById(R.id.task_time);
            tvTaskCategory = itemView.findViewById(R.id.task_category);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View view) {
            if (itemListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    itemListener.recycleViewListClicked(view, this.getLayoutPosition());
                }

            }

        }


        @Override
        public boolean onLongClick(View view) {
            Toast.makeText(view.getContext(),"Test", Toast.LENGTH_LONG).show();
            Log.d("DEBUGTHIS","on click runs");
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    mListener.onItemLongClick(position);
                }

            }
            return true;
        }
    }

        /*@Override
        public boolean onLongClick(View view) {
        return false;
        }


    }
*/
    public interface OnItemClickListener {
        void onItemLongClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

}
