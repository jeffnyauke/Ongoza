package com.piestack.ongoza.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.piestack.ongoza.R;
import com.piestack.ongoza.models.Summary;
import com.piestack.ongoza.models.data.Report;
import com.piestack.ongoza.utils.L;
import com.piestack.ongoza.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class FilteredReportsPartnerAdapter extends RecyclerView.Adapter<FilteredReportsPartnerAdapter.MyViewHolder> {
    private Context mContext;
    private List<Summary> messages;
    private List<Report> messagesQuery;
    private MessageAdapterListener listener;
    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView tvpartner, tvstheme, tvhrsbds, tvhrstravel;
        public LinearLayout messageContainer;

        public MyViewHolder(View view) {
            super(view);
            tvpartner = (TextView) view.findViewById(R.id.tvpartner);
            tvstheme = (TextView) view.findViewById(R.id.tvstheme);
            tvhrsbds = (TextView) view.findViewById(R.id.tvhrsbds);
            tvhrstravel = (TextView) view.findViewById(R.id.tvhrstravel);
            messageContainer = (LinearLayout) view.findViewById(R.id.message_container) ;
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }


    public FilteredReportsPartnerAdapter(Context mContext, List<Summary> messages, MessageAdapterListener listener) {
        this.mContext = mContext;
        this.messages = messages;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }
 
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filtered_report_list_row_partners, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Summary message = messages.get(position);

        Log.e("Adapter",message.getStheme()+message.getHrs_bds()+message.getHrs_travel()+message.getPartners());
 
        // displaying text view data
        if(!message.getStheme().equals("-")) {
            holder.tvstheme.setText(StringUtils.isNullorEmpty(message.getStheme()) ? "" : String.format("%.0f", Double.valueOf(message.getStheme())));
        }else{
            holder.tvstheme.setText(StringUtils.isNullorEmpty(message.getStheme()) ? "" : message.getStheme());
        }
        holder.tvhrsbds.setText(StringUtils.isNullorEmpty(message.getHrs_bds()) ?"": String.format("%.1f",Double.valueOf(message.getHrs_bds())));
        holder.tvhrstravel.setText(StringUtils.isNullorEmpty(message.getHrs_travel()) ? "": String.format("%.1f",Double.valueOf(message.getHrs_travel())));
        holder.tvpartner.setText(StringUtils.isNullorEmpty(message.getPartnerName()) ? "": message.getPartnerName());

 
        // displaying the first letter of From in icon text
        //holder.iconText.setText(message.getTitle().substring(0, 1));
 
        // change the row state to activated
        holder.itemView.setActivated(selectedItems.get(position, false));

        applyClickEvents(holder, position);
    }
 
    private void applyClickEvents(MyViewHolder holder, final int position) {

 
        holder.messageContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onMessageRowClicked(position);
            }
        });
 
        holder.messageContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }


 
    // As the views will be reused, sometimes the icon appears as
    // flipped because older view is reused. Reset the Y-axis to 0
    private void resetIconYAxis(View view) {
        if (view.getRotationY() != 0) {
            view.setRotationY(0);
        }
    }
 
    public void resetAnimationIndex() {
        reverseAllAnimations = false;
        animationItemsIndex.clear();
    }
 
    @Override
    public long getItemId(int position) {
        return Integer.valueOf(messages.get(position).getId());
    }
 
    private void applyImportant(MyViewHolder holder, Report message) {

    }
 
    private void applyReadStatus(MyViewHolder holder, Report message) {

    }
 
    @Override
    public int getItemCount() {
        return messages.size();
    }
 
    public void toggleSelection(int pos) {
        currentSelectedIndex = pos;
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
            animationItemsIndex.delete(pos);
        } else {
            selectedItems.put(pos, true);
            animationItemsIndex.put(pos, true);
        }
        notifyItemChanged(pos);
    }
 
    public void clearSelections() {
        reverseAllAnimations = true;
        selectedItems.clear();
        notifyDataSetChanged();
    }
 
    public int getSelectedItemCount() {
        return selectedItems.size();
    }
 
    public List<Integer> getSelectedItems() {
        List<Integer> items =
                new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }
 
    public void removeData(int position) {
        messages.remove(position);
        resetCurrentIndex();
    }
 
    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }
 
    public interface MessageAdapterListener {
        void onIconClicked(int position);
 
        void onIconImportantClicked(int position);
 
        void onMessageRowClicked(int position);
 
        void onRowLongClicked(int position);
    }

    public void animateTo(List<Report> reports) {
        applyAndAnimateRemovals(reports);
        applyAndAnimateAdditions(reports);
        applyAndAnimateMovedItems(reports);
    }

    private void applyAndAnimateRemovals(List<Report> newReports) {
        /*for (int i = messages.size() - 1; i >= 0; i--) {
            final Report report = messages.get(i);
            if (!newReports.contains(report)) {
                removeItem(i);
            }
        }*/
    }

    private void applyAndAnimateAdditions(List<Report> newReports) {
        for (int i = 0, count = messages.size(); i < count; i++) {
            final Report report = newReports.get(i);
            if (!messages.contains(report)) {
                   // addItem(i, report);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Report> newReports) {
        /*for (int toPosition = messages.size() - 1; toPosition >= 0; toPosition--) {
            final Report report = messages.get(toPosition);
            final int fromPosition = messages.indexOf(newReports);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }*/
    }

/*    public Summary removeItem(int position) {
       *//* final Summary contacts = messages.remove(position);
        L.e("Remove",contacts.getCountyName()+contacts.getSName());
        notifyItemRemoved(position);
        return contacts;*//*
    }*/

    public void addItem(int position, Summary report) {
        messages.add(position, report);
        L.e("Add",report.getPartners()+report.getHrs_bds());
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        /*final Report model = messages.remove(fromPosition);
        L.e("Move",model.getCountyName()+model.getSName()+fromPosition+toPosition);
        messages.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);*/
    }
}