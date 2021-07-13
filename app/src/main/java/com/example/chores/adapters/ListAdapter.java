package com.example.chores.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chores.databinding.ItemChoreBinding;
import com.example.chores.models.Chore;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    public static final String TAG = "ListAdapter";

    private Context context;
    private List<Chore> chores;
    ItemChoreBinding binding;

    public ListAdapter(Context context, List<Chore> chores) {
        this.context = context;
        this.chores = chores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemChoreBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ListAdapter.ViewHolder holder, int position) {
        Chore chore = chores.get(position);
        holder.bind(chore);
    }

    @Override
    public int getItemCount() {
        return chores.size();
    }

    public void clear() {
        chores.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Chore> list) {
        chores.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = binding.tvName;
            tvDescription = binding.tvDescription;
        }

        public void bind(Chore chore) {
            tvName.setText(chore.getName());
            tvDescription.setText(chore.getDescription());
        }
    }
}