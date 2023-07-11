package adb.gambler.adbapplication.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import adb.gambler.adbapplication.R;

/**
 * <p> File description: <p>
 * <p> Creator: Gambler   <p>
 * <p> Created date: 2022/3/24 <p>
 * * * * * * * * * * * * * * * * * * * * * *
 * Thinking is more important than coding. *
 * * * * * * * * * * * * * * * * * * * * * *
 */
public class RVAdapter extends RecyclerView.Adapter<RVViewHolder> {

    @NonNull
    @Override
    public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RVViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rv, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolder holder, int position) {
        holder.update(position);
    }

    @Override
    public int getItemCount() {
        return 10000;
    }
}
