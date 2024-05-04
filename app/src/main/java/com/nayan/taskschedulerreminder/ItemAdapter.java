package com.nayan.taskschedulerreminder;

import static kotlin.random.RandomKt.Random;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.nayan.taskschedulerreminder.Room.ModelClass;
import com.nayan.taskschedulerreminder.Room.TaskDatabase;
import com.nayan.taskschedulerreminder.Room.TasksDao;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    List<ModelClass> dataList;
    Context context;
    TaskDatabase database;
    TasksDao tasksDao;

    public ItemAdapter(List<ModelClass> list, Context context){

        this.dataList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ModelClass modelClass = dataList.get(position);

        holder.add_task.setText(modelClass.getTask());
        holder.add_date.setText(modelClass.getDate());
        holder.add_time.setText(modelClass.getTime());
        holder.task_message.setText(modelClass.getTask_message());

        //        set random color
        holder.cardView.setCardBackgroundColor(holder.itemView.getResources().getColor(randomColor(), null));

        database = TaskDatabase.getDatabase(context);
        tasksDao = database.tasksDao();

        holder.update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, UpdateTask.class);
                intent.putExtra("KEY_ID",modelClass.id);
                intent.putExtra("KEY_TASK",modelClass.task);
                intent.putExtra("KEY_DATE",modelClass.date);
                intent.putExtra("KEY_TIME",modelClass.time);
                intent.putExtra("KEY_MESSAGE",modelClass.task_message);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

                Toast.makeText(context, "Edit Your Task", Toast.LENGTH_SHORT).show();
            }
        });
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tasksDao.deleteTasks(dataList.get(position));
                Toast.makeText(context, "Task Delete Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ShowData.class);
                intent.putExtra("KEY_TASK",modelClass.task);
                intent.putExtra("KEY_DATE",modelClass.date);
                intent.putExtra("KEY_TIME",modelClass.time);
                intent.putExtra("KEY_MESSAGE",modelClass.task_message);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView add_task,add_date,add_time;
        ImageView update_btn, delete_btn;
        CardView cardView;
        TextView task_message;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            add_task = itemView.findViewById(R.id.add_task);
            add_date = itemView.findViewById(R.id.add_date);
            add_time = itemView.findViewById(R.id.add_time);
            update_btn = itemView.findViewById(R.id.edit);
            delete_btn = itemView.findViewById(R.id.delete);
            task_message = itemView.findViewById(R.id.task_message);
            cardView = itemView.findViewById(R.id.CardView);

        }
    };

    private int randomColor() {

        ArrayList<Integer> list = new ArrayList<>();
        list.add(R.color.NoteColor2);
        list.add(R.color.NoteColor3);
        list.add(R.color.NoteColor4);
        list.add(R.color.NoteColor5);
        list.add(R.color.NoteColor6);

        int seed = (int) System.currentTimeMillis();

        int randomIndex = Random(seed).nextInt(list.size());
        return list.get(randomIndex);

    }
}
