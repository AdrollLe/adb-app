package adb.gambler.adbapplication.view;

import android.view.View;
import android.widget.TextView;

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
public class RVViewHolder extends RecyclerView.ViewHolder {

    private TextView textView;

    public RVViewHolder(@NonNull View itemView) {
        super(itemView);

        textView = itemView.findViewById(R.id.item_tv);
    }

    public void update(String msg){
        textView.setText(msg);

        switch (msg.length() / 3){
            case 0:
                textView.setBackgroundResource(R.color.purple_700);
                break;
            case 1:
                textView.setBackgroundResource(R.color.black);
                break;
            case 2:
                textView.setBackgroundResource(R.color.teal_200);
                break;
            default:
                break;
        }
    }
}
