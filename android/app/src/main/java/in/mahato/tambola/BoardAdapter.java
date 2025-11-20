package in.mahato.tambola;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Set;
import java.util.HashSet;

import in.mahato.tambola.util.ScreenUtils;


public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.VH> {
    private Context ctx;
    private int[] nums;
    private Set<Integer> called = new HashSet<>();
    private Integer lastCalled = null;

    private Integer height;
    private Integer width;
    private static final String TAG = "GameActivity";



    public BoardAdapter(Context ctx) {
        this.ctx = ctx;
        nums = new int[90];
        for (int i = 0; i < 90; i++) nums[i] = i + 1;
        Activity activity= (Activity) ctx;
        Double screenheight = ScreenUtils.getScreenHeightPx(activity)*0.45*0.1;
        Double screenwidth = ScreenUtils.getScreenWidthPx(activity)*0.9*0.1;
        height = screenheight.intValue();
        width =screenwidth.intValue();

        Log.i(TAG, "screenheight"+ScreenUtils.getScreenHeightPx(activity));
        Log.i(TAG, "screenwidth"+ScreenUtils.getScreenWidthPx(activity));
           Log.i(TAG, "height"+height);
        Log.i(TAG, "weight"+width);
    }


    public void setCalled(Set<Integer> called, Integer last) {
        this.called = called == null ? new HashSet<Integer>() : called;
        this.lastCalled = last;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(ctx).inflate(R.layout.item_board_number, parent, false);
        return new VH(v);
    }


    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        int num = nums[position];
        holder.tv.setText(String.valueOf(num));

        ViewGroup.LayoutParams params = holder.tv.getLayoutParams();

// Cast the params to the specific type for the parent layout (e.g., LinearLayout.LayoutParams)
// This is important if you need to access properties specific to that layout type
        if (params instanceof RecyclerView.LayoutParams) {
            RecyclerView.LayoutParams linearParams = (RecyclerView.LayoutParams) params;

            // Set the width
            linearParams.width = width;
            linearParams.height=height;// Set a specific width in pixels
            // Or use constants:
            // linearParams.width = LayoutParams.MATCH_PARENT;
            // linearParams.width = LayoutParams.WRAP_CONTENT;

            // You can also set other properties like margins if needed
            // linearParams.setMargins(10, 20, 10, 20); // left, top, right, bottom in pixels

            // Apply the updated LayoutParams back to the TextView
            holder.tv.setLayoutParams(linearParams);
        }


        if (lastCalled != null && lastCalled == num) {
            holder.tv.setBackgroundColor(0xFF00BCD4); // cyan-like for last
           // holder.tv.setBackgroundColor(Color.parseColor("#FFFF00"));
        } else if (called.contains(num)) {
            holder.tv.setBackgroundColor(Color.parseColor("#FF0000"));
          //  holder.tv.setBackgroundColor(0xFF4CAF50); // green for called
        } else {
            holder.tv.setBackgroundColor(0xFF4A007E);
          //  holder.tv.setBackgroundColor(0xFF888888); // default grey
        }
    }


    @Override
    public int getItemCount() { return nums.length; }


    static class VH extends RecyclerView.ViewHolder {
        TextView tv;
        VH(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tvNum);
            var size = tv.getTextSize();

        }
    }
}