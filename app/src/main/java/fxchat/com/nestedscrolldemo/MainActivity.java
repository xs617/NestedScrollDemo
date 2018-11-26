package fxchat.com.nestedscrolldemo;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NestedScrollParentView nestedScrollParentView;
    RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_list);
        nestedScrollParentView = findViewById(R.id.pull_to_refresh_recycler_view);
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
        List<String> data = new ArrayList<>();

        {
            for (int i = 0; i < 100; i++) {
                data.add(String.valueOf(i));
            }
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_list_item_1, viewGroup, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
            myViewHolder.bind(data.get(i));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView line;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            line = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String data){
            line.setText(data);
        }
    }
}
