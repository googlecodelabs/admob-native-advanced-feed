/*
 * Copyright (C) 2017 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.android.gms.example.nativeadvancedrecyclerviewexample;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.ads.formats.NativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.List;

/**
 * The {@link RecyclerViewAdapter} class.
 * <p>The adapter provides access to the items in the {@link MenuItemViewHolder}
 */
class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

    // An Activity's Context.
    private final Context mContext;

    // The list of Native ads and menu items.
    private final List<Object> mRecyclerViewItems;

    /**
     * For this example app, the recyclerViewItems list contains only
     * {@link MenuItem} and {@link UnifiedNativeAd} types.
     */
    public RecyclerViewAdapter(Context context, List<Object> recyclerViewItems) {
        this.mContext = context;
        this.mRecyclerViewItems = recyclerViewItems;
    }

    /**
     * The {@link MenuItemViewHolder} class.
     * Provides a reference to each view in the menu item view.
     */
    public class MenuItemViewHolder extends RecyclerView.ViewHolder {
        private TextView menuItemName;
        private TextView menuItemDescription;
        private TextView menuItemPrice;
        private TextView menuItemCategory;
        private ImageView menuItemImage;

        MenuItemViewHolder(View view) {
            super(view);
            menuItemImage = (ImageView) view.findViewById(R.id.menu_item_image);
            menuItemName = (TextView) view.findViewById(R.id.menu_item_name);
            menuItemPrice = (TextView) view.findViewById(R.id.menu_item_price);
            menuItemCategory = (TextView) view.findViewById(R.id.menu_item_category);
            menuItemDescription = (TextView) view.findViewById(R.id.menu_item_description);
        }
    }

    @Override
    public int getItemCount() {
        return mRecyclerViewItems.size();
    }

    /**
     * Determines the view type for the given position.
     */
    @Override
    public int getItemViewType(int position) {

        Object recyclerViewItem = mRecyclerViewItems.get(position);
        if (recyclerViewItem instanceof UnifiedNativeAd) {
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }

    /**
     * Creates a new view for a menu item view or a Native ad view
     * based on the viewType. This method is invoked by the layout manager.
     */
@Override
public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    switch (viewType) {
        case UNIFIED_NATIVE_AD_VIEW_TYPE:
            View unifiedNativeLayoutView = LayoutInflater.from(
                viewGroup.getContext()).inflate(R.layout.ad_unified,
                viewGroup, false);
            return new UnifiedNativeAdViewHolder(unifiedNativeLayoutView);
        case MENU_ITEM_VIEW_TYPE:
            // Fall through.
        default:
            View menuItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.menu_item_container, viewGroup, false);
            return new MenuItemViewHolder(menuItemLayoutView);
    }
}

    /**
     * Replaces the content in the views that make up the menu item view and the
     * Native ad view. This method is invoked by the layout manager.
     */
@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
    int viewType = getItemViewType(position);
    switch (viewType) {
        case UNIFIED_NATIVE_AD_VIEW_TYPE:
            UnifiedNativeAd nativeAd = (UnifiedNativeAd) mRecyclerViewItems.get(position);
            populateNativeAdView(nativeAd, ((UnifiedNativeAdViewHolder) holder).getAdView());
            break;
        case MENU_ITEM_VIEW_TYPE:
            // fall through
        default:
            MenuItemViewHolder menuItemHolder = (MenuItemViewHolder) holder;
            MenuItem menuItem = (MenuItem) mRecyclerViewItems.get(position);

            // Get the menu item image resource ID.
            String imageName = menuItem.getImageName();
            int imageResID = mContext.getResources().getIdentifier(imageName, "drawable",
                mContext.getPackageName());

            // Add the menu item details to the menu item view.
            menuItemHolder.menuItemImage.setImageResource(imageResID);
            menuItemHolder.menuItemName.setText(menuItem.getName());
            menuItemHolder.menuItemPrice.setText(menuItem.getPrice());
            menuItemHolder.menuItemCategory.setText(menuItem.getCategory());
            menuItemHolder.menuItemDescription.setText(menuItem.getDescription());
    }
}

private void populateNativeAdView(UnifiedNativeAd nativeAd,
                                  UnifiedNativeAdView adView) {
    // Some assets are guaranteed to be in every UnifiedNativeAd.
    ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
    ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
    ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());

    // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
    // check before trying to display them.
    NativeAd.Image icon = nativeAd.getIcon();

    if (icon == null) {
        adView.getIconView().setVisibility(View.INVISIBLE);
    } else {
        ((ImageView) adView.getIconView()).setImageDrawable(icon.getDrawable());
        adView.getIconView().setVisibility(View.VISIBLE);
    }

    if (nativeAd.getPrice() == null) {
        adView.getPriceView().setVisibility(View.INVISIBLE);
    } else {
        adView.getPriceView().setVisibility(View.VISIBLE);
        ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
    }

    if (nativeAd.getStore() == null) {
        adView.getStoreView().setVisibility(View.INVISIBLE);
    } else {
        adView.getStoreView().setVisibility(View.VISIBLE);
        ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
    }

    if (nativeAd.getStarRating() == null) {
        adView.getStarRatingView().setVisibility(View.INVISIBLE);
    } else {
        ((RatingBar) adView.getStarRatingView())
            .setRating(nativeAd.getStarRating().floatValue());
        adView.getStarRatingView().setVisibility(View.VISIBLE);
    }

    if (nativeAd.getAdvertiser() == null) {
        adView.getAdvertiserView().setVisibility(View.INVISIBLE);
    } else {
        ((TextView) adView.getAdvertiserView()).setText(nativeAd.getAdvertiser());
        adView.getAdvertiserView().setVisibility(View.VISIBLE);
    }

    // Assign native ad object to the native view.
    adView.setNativeAd(nativeAd);
}
}
