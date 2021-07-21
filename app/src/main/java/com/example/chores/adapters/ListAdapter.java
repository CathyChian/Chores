package com.example.chores.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chores.R;
import com.example.chores.activities.ChoreDetailsActivity;
import com.example.chores.activities.EditActivity;
import com.example.chores.databinding.ItemChoreBinding;
import com.example.chores.fragments.ListFragment;
import com.example.chores.models.Chore;
import com.zerobranch.layout.SwipeLayout;

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

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvDescription;
        private TextView tvRecurring;
        private TextView tvDateDue;
        private TextView tvSharedUsers;
        private ImageView leftIvEdit;
        private ImageView rightIvDelete;
        private RelativeLayout itemChore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = binding.tvName;
            tvDescription = binding.tvDescription;
            tvRecurring = binding.tvRecurring;
            tvDateDue = binding.tvDateDue;
            tvSharedUsers = binding.tvSharedUsers;
            leftIvEdit = binding.leftIvEdit;
            rightIvDelete = binding.rightIvDelete;
            itemChore = binding.itemChore;
        }

        public void bind(Chore chore) {
            tvName.setText(chore.getName());
            tvDescription.setText(chore.getDescription());
            tvRecurring.setText(chore.getRecurringText());
            tvDateDue.setText(chore.getRelativeDateText());
            tvSharedUsers.setText(chore.getListOfUsers());

            leftIvEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchEdit(chore);
                }
            });

            rightIvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteChore(chore);
                }
            });

            itemChore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    launchDetailedView(chore);
                }
            });
        }

        public void launchDetailedView(Chore chore) {
            if (getAdapterPosition() == RecyclerView.NO_POSITION) { return; }
            Intent intent = new Intent(context, ChoreDetailsActivity.class);
            intent.putExtra("chore", Parcels.wrap(chore));
            intent.putExtra("position", getAdapterPosition());
            fragment.startActivityForResult(intent, ListFragment.DETAILED_REQUEST_CODE);
        }

        public void launchEdit(Chore chore) {
            Intent intent = new Intent(context, EditActivity.class);
            intent.putExtra("chore", Parcels.wrap(chore));
            intent.putExtra("position", getAdapterPosition());
            fragment.startActivityForResult(intent, ListFragment.UPDATE_REQUEST_CODE);
        }

        public void deleteChore(Chore chore) {
            chore.deleteInBackground();
            chores.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
        }
    }
}