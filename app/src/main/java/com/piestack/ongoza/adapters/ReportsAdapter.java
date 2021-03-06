package com.piestack.ongoza.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.piestack.ongoza.R;
import com.piestack.ongoza.helper.CircleTransform;
import com.piestack.ongoza.helper.FlipAnimator;
import com.piestack.ongoza.models.data.Report;
import com.piestack.ongoza.utils.DateConverter;
import com.piestack.ongoza.utils.L;

import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyUtils;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Report> messages;
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
        public TextView from, subject, message, iconText, timestamp;
        public ImageView iconImp, imgProfile;
        public LinearLayout messageContainer;
        public RelativeLayout iconContainer, iconBack, iconFront;
 
        public MyViewHolder(View view) {
            super(view);
            from = (TextView) view.findViewById(R.id.from);
            subject = (TextView) view.findViewById(R.id.txt_primary);
            message = (TextView) view.findViewById(R.id.txt_secondary);
            //iconText = (TextView) view.findViewById(R.id.icon_text);
            timestamp = (TextView) view.findViewById(R.id.timestamp);
            //iconBack = (RelativeLayout) view.findViewById(R.id.icon_back);
            //iconFront = (RelativeLayout) view.findViewById(R.id.icon_front);
            iconImp = (ImageView) view.findViewById(R.id.icon_star);
            //imgProfile = (ImageView) view.findViewById(R.id.icon_profile);
            messageContainer = (LinearLayout) view.findViewById(R.id.message_container);
            //iconContainer = (RelativeLayout) view.findViewById(R.id.icon_container);
            view.setOnLongClickListener(this);
        }
 
        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }
 
 
    public ReportsAdapter(Context mContext, List<Report> messages, MessageAdapterListener listener) {
        this.mContext = mContext;
        this.messages = messages;
        this.messagesQuery = messages;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }
 
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_list_row, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Report message = messages.get(position);
 
        // displaying text view data
        holder.from.setText(message.getPName());
        holder.subject.setText(message.getCountyName()+", "+message.getSName());
        holder.message.setText(message.getSubName());
        if(message.getDor() != null) {
            holder.timestamp.setText(DateConverter.toPrettyDate(Long.valueOf(message.getDor()).longValue()));
        }
 
        // displaying the first letter of From in icon text
        //holder.iconText.setText(message.getTitle().substring(0, 1));
 
        // change the row state to activated
        holder.itemView.setActivated(selectedItems.get(position, false));
 
        // change the font style depending on message read status
        applyReadStatus(holder, message);
 
        // handle message star
        applyImportant(holder, message);
 
        // handle icon animation
        //applyIconAnimation(holder, position);
 
        // display profile image
        //applyProfilePicture(holder, message);
 
        // apply click events
        applyClickEvents(holder, position);
    }
 
    private void applyClickEvents(MyViewHolder holder, final int position) {
        /*holder.iconContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconClicked(position);
            }
        });*/
 
        holder.iconImp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onIconImportantClicked(position);
            }
        });
 
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
 
/*    private void applyProfilePicture(MyViewHolder holder, Report message) {
        if (!TextUtils.isEmpty(message.getPicture())) {
            Glide.with(mContext).load(message.getPicture())
                    .thumbnail(0.5f)
                    .crossFade()
                    .transform(new CircleTransform(mContext))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgProfile);
            holder.imgProfile.setColorFilter(null);
            holder.iconText.setVisibility(View.GONE);
        } else {
            holder.imgProfile.setImageResource(R.drawable.bg_circle);
            holder.imgProfile.setColorFilter(message.getColor());
            holder.iconText.setVisibility(View.VISIBLE);
        }
    }*/
 
    private void applyIconAnimation(MyViewHolder holder, int position) {
        if (selectedItems.get(position, false)) {
            holder.iconFront.setVisibility(View.GONE);
            resetIconYAxis(holder.iconBack);
            holder.iconBack.setVisibility(View.VISIBLE);
            holder.iconBack.setAlpha(1);
            if (currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, true);
                resetCurrentIndex();
            }
        } else {
            holder.iconBack.setVisibility(View.GONE);
            resetIconYAxis(holder.iconFront);
            holder.iconFront.setVisibility(View.VISIBLE);
            holder.iconFront.setAlpha(1);
            if ((reverseAllAnimations && animationItemsIndex.get(position, false)) || currentSelectedIndex == position) {
                FlipAnimator.flipView(mContext, holder.iconBack, holder.iconFront, false);
                resetCurrentIndex();
            }
        }
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
        if (message.isImportant()) {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_black_24dp));
            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_selected));
        } else {
            holder.iconImp.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_star_border_black_24dp));
            holder.iconImp.setColorFilter(ContextCompat.getColor(mContext, R.color.icon_tint_normal));
        }
    }
 
    private void applyReadStatus(MyViewHolder holder, Report message) {
        Typeface typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/Lato-Semibold.ttf");
        if (message.isRead()) {
            holder.from.setTypeface(null, Typeface.NORMAL);
            holder.subject.setTypeface(null, Typeface.NORMAL);
            holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
            holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.message));
        } else {
            holder.from.setTypeface(null, Typeface.BOLD);
            holder.subject.setTypeface(null, Typeface.BOLD);
            CalligraphyUtils.applyFontToTextView(holder.from, typeface);
            CalligraphyUtils.applyFontToTextView(holder.subject, typeface);

            holder.from.setTextColor(ContextCompat.getColor(mContext, R.color.from));
            holder.subject.setTextColor(ContextCompat.getColor(mContext, R.color.subject));
        }
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
        for (int i = messages.size() - 1; i >= 0; i--) {
            final Report report = messages.get(i);
            if (!newReports.contains(report)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Report> newReports) {
        for (int i = 0, count = messages.size(); i < count; i++) {
            final Report report = newReports.get(i);
            if (!messages.contains(report)) {
                    addItem(i, report);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Report> newReports) {
        for (int toPosition = messages.size() - 1; toPosition >= 0; toPosition--) {
            final Report report = messages.get(toPosition);
            final int fromPosition = messages.indexOf(newReports);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Report removeItem(int position) {
        final Report contacts = messages.remove(position);
        L.e("Remove",contacts.getCountyName()+contacts.getSName());
        notifyItemRemoved(position);
        return contacts;
    }

    public void addItem(int position, Report report) {
        messages.add(position, report);
        L.e("Add",report.getCountyName()+report.getSName());
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Report model = messages.remove(fromPosition);
        L.e("Move",model.getCountyName()+model.getSName()+fromPosition+toPosition);
        messages.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}