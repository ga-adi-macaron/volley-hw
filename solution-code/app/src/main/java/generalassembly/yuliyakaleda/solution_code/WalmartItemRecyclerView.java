package generalassembly.yuliyakaleda.solution_code;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import generalassembly.yuliyakaleda.solution_code.model.WalmartItem;

/**
 * Created by charlie on 11/18/16.
 */

public class WalmartItemRecyclerView
        extends RecyclerView.Adapter<WalmartItemRecyclerView.WalmartItemViewHolder> {

    private List<WalmartItem> mWalmartItems;

    public WalmartItemRecyclerView(List<WalmartItem> walmartItems) {
        mWalmartItems = walmartItems;
    }

    @Override
    public WalmartItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new WalmartItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WalmartItemViewHolder holder, int position) {
        holder.mTextView.setText(mWalmartItems.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mWalmartItems.size();
    }

    public class WalmartItemViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public WalmartItemViewHolder(View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(android.R.id.text1);
        }
    }
}
