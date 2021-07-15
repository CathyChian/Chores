package com.example.chores.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chores.activities.ChoreDetailsActivity;
import com.example.chores.databinding.ItemChoreBinding;
import com.example.chores.fragments.ListFragment;
import com.example.chores.models.Chore;

import org.parceler.Parcels;

import java.util.List;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    public static final String TAG = "ListAdapter";

    private Context context;
    private List<Chore> chores;
    ItemChoreBinding binding;
    ListFragment fragment;

    public ListAdapter(Context context, ListFragment fragment, List<Chore> chores) {
        this.context = context;
        this.fragment = fragment;
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

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvName;
        private TextView tvDescription;
        private ImageView ivDelete;
        private TextView tvRecurring;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = binding.tvName;
            tvDescription = binding.tvDescription;
            ivDelete = binding.ivDelete;
            tvRecurring = binding.tvRecurring;

            itemView.setOnClickListener(this);
        }

        public void bind(Chore chore) {
            tvName.setText(chore.getName());
            tvDescription.setText(chore.getDescription());
            setRecurring(chore);

            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteChore(chore);
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) { return; }

            Chore chore = chores.get(getAdapterPosition());
            Intent intent = new Intent(context, ChoreDetailsActivity.class);
            intent.putExtra("chore", Parcels.wrap(chore));
            intent.putExtra("position", getAdapterPosition());
            fragment.startActivityForResult(intent, fragment.UPDATE_REQUEST_CODE);

            // TODO: Update adapter after editing chore
        }

        public void setRecurring(Chore chore) {
            if (chore.isRecurring()) {
                tvRecurring.setText("Repeats every " + chore.getFrequency() + " days");
            } else {
                tvRecurring.setText("");
            }
        }

        public void deleteChore(Chore chore) {
            chore.deleteInBackground();
            chores.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
        }
    }
}