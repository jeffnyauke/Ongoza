package com.piestack.ongoza.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.piestack.ongoza.R;
import com.piestack.ongoza.helper.CircleTransform;
import com.piestack.ongoza.helper.FlipAnimator;
import com.piestack.ongoza.models.Options;
import com.piestack.ongoza.models.Report;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.MyViewHolder> {
    private Context mContext;
    private List<Options> optionsList;
    private OptionsAdapterListener listener;
    private SparseBooleanArray selectedItems;

    // array used to perform multiple animation at once
    private SparseBooleanArray animationItemsIndex;
    private boolean reverseAllAnimations = false;

    // index is used to animate only the selected row
    // dirty fix, find a better solution
    private static int currentSelectedIndex = -1;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        @BindView(R.id.image) ImageView image;
        @BindView(R.id.title) TextView title;
        @BindView(R.id.subtitle) TextView subtitle;
        @BindView(R.id.card) CardView card;
        @BindView(R.id.frameLayout)
        FrameLayout frameLayout;

        public MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            listener.onRowLongClicked(getAdapterPosition());
            view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
            return true;
        }
    }


    public OptionsAdapter(Context mContext, List<Options> optionsList, OptionsAdapterListener listener) {
        this.mContext = mContext;
        this.optionsList = optionsList;
        this.listener = listener;
        selectedItems = new SparseBooleanArray();
        animationItemsIndex = new SparseBooleanArray();
    }
 
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.options_item_row, parent, false);
 
        return new MyViewHolder(itemView);
    }
 
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        Resources resources = mContext.getResources();
        Options option = optionsList.get(position);
 
        // displaying text view data
        holder.title.setText(option.getTitle());
        holder.subtitle.setText(option.getSubtitle());
        /*Glide.with(mContext).load(getDrawable(position)).crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.image);*/
        holder.image.setImageDrawable(resources.getDrawable(getDrawable(position)));
        holder.frameLayout.setBackgroundColor(getRandomMaterialColor("400"));
        // apply click events
        applyClickEvents(holder, position);
    }
 
    private void applyClickEvents(MyViewHolder holder, final int position) {
 
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onOptionRowClicked(position);
            }
        });
 
        holder.card.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onRowLongClicked(position);
                view.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);
                return true;
            }
        });
    }

    public int getDrawable(int position){
        int drawable;

        switch (position) {
            case 0:
                drawable = R.drawable.ic_create_black_24dp;
                break;
            case 1:
                drawable = R.drawable.date_icon;
                break;
            case 2:
                drawable = R.drawable.month_icon;
                break;
            case 3:
                drawable = R.drawable.reports_icon;
                break;
            case 4:
                drawable = R.drawable.date_icon;
                break;
            default:
                drawable = R.drawable.bg_circle;
        }
        return drawable;
    }
 
    @Override
    public long getItemId(int position) {
        return optionsList.get(position).getId();
    }
 
    @Override
    public int getItemCount() {
        return optionsList.size();
    }

 
    public void removeData(int position) {
        optionsList.remove(position);
        resetCurrentIndex();
    }
 
    private void resetCurrentIndex() {
        currentSelectedIndex = -1;
    }
 
    public interface OptionsAdapterListener {
 
        void onOptionRowClicked(int position);
 
        void onRowLongClicked(int position);
    }

    /**
     * chooses a random color from array.xml
     */
    private int getRandomMaterialColor(String typeColor) {
        int returnColor = Color.GRAY;
        int arrayId = mContext.getResources().getIdentifier("mdcolor_" + typeColor, "array", mContext.getPackageName());

        if (arrayId != 0) {
            TypedArray colors = mContext.getResources().obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.GRAY);
            colors.recycle();
        }
        return returnColor;
    }
}