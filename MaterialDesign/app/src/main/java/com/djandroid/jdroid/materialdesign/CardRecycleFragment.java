package com.djandroid.jdroid.materialdesign;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;



import java.util.ArrayList;


/**
 * Created by dhawal sodha parmar on 5/4/2015.
 */
public class CardRecycleFragment extends Fragment {

    ArrayList<CardViewModel> listitems = new ArrayList<CardViewModel>();
    //String items[] = {"Dhaval Sodha Parmar", "b", "v"};
    RecyclerView recList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupListItems();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cardrecycle, container, false);

        RecyclerView recList = (RecyclerView) rootView.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        if (listitems.size() > 0 & recList != null) {
            recList.setAdapter(new MyAdapter(listitems));
        }
        recList.setLayoutManager(llm);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
        private ArrayList<CardViewModel> list;

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(ArrayList<CardViewModel> myDataset) {
            list = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycle_items, parent, false);
            // set the view's size, margins, paddings and layout parameters

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.text_card_name.setText(list.get(position).getCardName());
            Drawable dr = getResources().getDrawable(list.get(position).getImageResourceId());
            holder.image_card_cover.setImageDrawable(dr);

            /*Palette.from(drawableToBitmap(dr)).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette p) {
                    // Use generated instance
                    //int titleBackColor = p.getVibrantColor(0);
                    int titleBackColor = p.getDarkVibrantColor(0);
                    //int titleBackColor = p.getVibrantSwatch();
                    ProgressDrawable titleBackDrawable;
                    if (titleBackColor != 0) {
                        titleBackDrawable = new ProgressDrawable(titleBackColor);
                    } else {
                        titleBackDrawable = new ProgressDrawable(getThemePrimaryColor());
                    }
                    //titleBackDrawable.setMax(holder.text_card_name.getHeight());
                    //titleBackDrawable.setProgress(holder.text_card_name.getHeight());
                    holder.text_card_name.setBackground(titleBackDrawable);
                }
            });*/


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return list.size();
        }
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView text_card_name;
        public ImageView image_card_cover;
        public ImageView image_action_like;
        public ImageView image_action_flag;
        public ImageView image_action_share;
        public Toolbar maintoolbar;

        public ViewHolder(View v) {
            super(v);
            text_card_name = (TextView) v.findViewById(R.id.text_card_name);
            image_card_cover = (ImageView) v.findViewById(R.id.image_card_cover);
            image_action_like = (ImageView) v.findViewById(R.id.image_action_like);
            image_action_flag = (ImageView) v.findViewById(R.id.image_action_flag);
            image_action_share = (ImageView) v.findViewById(R.id.image_action_share);
            maintoolbar = (Toolbar) v.findViewById(R.id.card_toolbar);
            maintoolbar.inflateMenu(R.menu.global);
        }
    }

    public void setupListItems() {
        listitems.clear();

        CardViewModel item1 = new CardViewModel();
        item1.setCardName("Dhawal Sodha Parmar");
        item1.setImageResourceId(R.drawable.my_pic);
        item1.setIsfav(0);
        item1.setIsturned(0);
        listitems.add(item1);

        CardViewModel item2 = new CardViewModel();
        item2.setCardName("Cart Item");
        item2.setImageResourceId(R.drawable.header_bg);
        item2.setIsfav(0);
        item2.setIsturned(0);
        listitems.add(item2);

        /*CardViewModel item3 = new CardViewModel();
        item3.setCardName("Cart Item");
        item3.setImageResourceId(R.drawable.fc_tab_bg_new);
        item3.setIsfav(0);
        item3.setIsturned(0);
        listitems.add(item3);*/

        CardViewModel item4 = new CardViewModel();
        item4.setCardName("Dhawal");
        item4.setImageResourceId(R.drawable.my_profile);
        item4.setIsfav(0);
        item4.setIsturned(0);
        listitems.add(item4);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }
        final Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private int getThemePrimaryColor() {
        final TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        int[] attribute = new int[]{R.attr.colorPrimary};
        final TypedArray array = getActivity().obtainStyledAttributes(typedValue.resourceId, attribute);
        return array.getColor(0, Color.MAGENTA);
    }
}
