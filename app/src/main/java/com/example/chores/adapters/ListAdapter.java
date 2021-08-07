package com.example.chores.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chores.R;
import com.example.chores.activities.ChoreDetailsActivity;
import com.example.chores.activities.EditActivity;
import com.example.chores.activities.MainActivity;
import com.example.chores.databinding.ItemChoreBinding;
import com.example.chores.fragments.CalendarFragment;
import com.example.chores.fragments.ListFragment;
import com.example.chores.models.Chore;

import org.parceler.Parcels;

import java.util.Calendar;
import java.util.List;

import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

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
        private TextView tvDateDue;
        private TextView tvRecurring;
        private RelativeLayout rlImportance;
        private CheckBox cbDone;
        private nl.dionsegijn.konfetti.KonfettiView konfetti;
        private ImageView leftIvEdit;
        private ImageView rightIvDelete;
        private RelativeLayout itemChore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = binding.tvName;
            tvDateDue = binding.tvDateDue;
            tvRecurring = binding.tvRecurring;
            rlImportance = binding.rlImportance;
            cbDone = binding.cbDone;
            konfetti = ((Activity) context).findViewById(R.id.konfetti);
            leftIvEdit = binding.leftIvEdit;
            rightIvDelete = binding.rightIvDelete;
            itemChore = binding.itemChore;
        }

        public void bind(Chore chore) {
            tvName.setText(chore.getName());
            tvDateDue.setText(chore.getRelativeDateText());
            tvRecurring.setText(chore.getRecurringText());
            setImportanceColor(chore.getWeight());

            cbDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cbDone.isChecked()) {
                        konfetti();
                        if (chore.isRecurring()) {
                            chore.setDateDue(Calendar.getInstance(), chore.getFrequency());
                            chore.setWeight(chore.getPriority(), Calendar.getInstance(), chore.getFrequency());
                            setImportanceColor(chore.getWeight());
                            chore.saveInBackground();
                            notifyItemChanged(getAdapterPosition());
                        } else {
                            deleteChore(chore);
                        }
                        cbDone.toggle();
                    }
                }
            });

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
            fragment.startActivityForResult(intent, MainActivity.DETAILED_REQUEST_CODE);
        }

        public void launchEdit(Chore chore) {
            Intent intent = new Intent(context, EditActivity.class);
            intent.putExtra("chore", Parcels.wrap(chore));
            intent.putExtra("position", getAdapterPosition());
            fragment.startActivityForResult(intent, MainActivity.UPDATE_REQUEST_CODE);
        }

        public void deleteChore(Chore chore) {
            chore.deleteInBackground();
            chores.remove(getAdapterPosition());
            CalendarFragment.choreEvents.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
        }

        public void setImportanceColor(double weight) {
            if (weight < -5) {
                rlImportance.setBackgroundColor(ContextCompat.getColor(context, R.color.blue));
            } else if (weight < 0) {
                rlImportance.setBackgroundColor(ContextCompat.getColor(context, R.color.green));
            } else if (weight == 0) {
                rlImportance.setBackgroundColor(ContextCompat.getColor(context, R.color.yellow));
            } else if (weight > 0) {
                rlImportance.setBackgroundColor(ContextCompat.getColor(context, R.color.orange));
            } else if (weight > 5) {
                rlImportance.setBackgroundColor(ContextCompat.getColor(context, R.color.red));
            }
        }

        public void konfetti() {
            konfetti.build()
                    .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                    .setDirection(0.0, 359.0)
                    .setSpeed(2f, 10f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT, Shape.CIRCLE)
                    .addSizes(new Size(12, 5f))
                    .setPosition(-50f, konfetti.getWidth() + 50f, -50f, -50f)
                    .streamFor(100, 1000L);
        }
    }
}