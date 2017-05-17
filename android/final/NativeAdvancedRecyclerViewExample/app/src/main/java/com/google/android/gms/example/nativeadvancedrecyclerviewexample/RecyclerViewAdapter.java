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
import com.google.android.gms.ads.formats.NativeAppInstallAd;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;
import com.google.android.gms.ads.formats.NativeContentAd;
import com.google.android.gms.ads.formats.NativeContentAdView;

import java.util.List;

/**
 * The {@link RecyclerViewAdapter} class.
 * <p>The adapter provides access to the items in the {@link MenuItemViewHolder}
 */
class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // A menu item view type.
    private static final int MENU_ITEM_VIEW_TYPE = 0;

    private static final int NATIVE_APP_INSTALL_AD_VIEW_TYPE = 1;

    private static final int NATIVE_CONTENT_AD_VIEW_TYPE = 2;

    // An Activity's Context.
    private final Context mContext;

    // The list of Native Express ads and menu items.
    private final List<Object> mRecyclerViewItems;

    /**
     * For this example app, the recyclerViewItems list contains only
     * {@link MenuItem} and {@link NativeAd} types.
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
        if (recyclerViewItem instanceof NativeAppInstallAd) {
            return NATIVE_APP_INSTALL_AD_VIEW_TYPE;
        } else if (recyclerViewItem instanceof NativeContentAd) {
            return NATIVE_CONTENT_AD_VIEW_TYPE;
        }
        return MENU_ITEM_VIEW_TYPE;
    }

    /**
     * Creates a new view for a menu item view or a Native Express ad view
     * based on the viewType. This method is invoked by the layout manager.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case NATIVE_APP_INSTALL_AD_VIEW_TYPE:
                View nativeAppInstallLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.ad_app_install,
                        viewGroup, false);
                return new NativeAppInstallAdViewHolder(nativeAppInstallLayoutView);
            case NATIVE_CONTENT_AD_VIEW_TYPE:
                View nativeContentLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.ad_content,
                        viewGroup, false);
                return new NativeContentAdViewHolder(nativeContentLayoutView);
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
     * Native Express ad view. This method is invoked by the layout manager.
     */
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case NATIVE_APP_INSTALL_AD_VIEW_TYPE:
                NativeAppInstallAd appInstallAd = (NativeAppInstallAd) mRecyclerViewItems.get(position);
                populateAppInstallAdView(appInstallAd, (NativeAppInstallAdView) holder.itemView);
                break;
            case NATIVE_CONTENT_AD_VIEW_TYPE:
                NativeContentAd contentAd = (NativeContentAd) mRecyclerViewItems.get(position);
                populateContentAdView(contentAd, (NativeContentAdView) holder.itemView);
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

    private void populateAppInstallAdView(NativeAppInstallAd nativeAppInstallAd,
                                          NativeAppInstallAdView adView) {
        // Some assets are guaranteed to be in every NativeAppInstallAd.
        ((ImageView) adView.getIconView()).setImageDrawable(nativeAppInstallAd.getIcon()
                .getDrawable());
        ((TextView) adView.getHeadlineView()).setText(nativeAppInstallAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeAppInstallAd.getBody());
        ((Button) adView.getCallToActionView()).setText(nativeAppInstallAd.getCallToAction());

        // These assets aren't guaranteed to be in every NativeAppInstallAd, so it's important to
        // check before trying to display them.
        if (nativeAppInstallAd.getPrice() == null) {
            adView.getPriceView().setVisibility(View.INVISIBLE);
        } else {
            adView.getPriceView().setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAppInstallAd.getPrice());
        }

        if (nativeAppInstallAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAppInstallAd.getStore());
        }

        if (nativeAppInstallAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAppInstallAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeAppInstallAd);
    }

    /**
     * Populates a {@link NativeContentAdView} object with data from a given
     * {@link NativeContentAd}.
     *
     * @param nativeContentAd the object containing the ad's assets
     * @param adView          the view to be populated
     */
    private void populateContentAdView(NativeContentAd nativeContentAd,
                                       NativeContentAdView adView) {
        // Some assets are guaranteed to be in every NativeContentAd.
        ((TextView) adView.getHeadlineView()).setText(nativeContentAd.getHeadline());
        ((TextView) adView.getBodyView()).setText(nativeContentAd.getBody());
        ((TextView) adView.getCallToActionView()).setText(nativeContentAd.getCallToAction());
        ((TextView) adView.getAdvertiserView()).setText(nativeContentAd.getAdvertiser());

        List<NativeAd.Image> images = nativeContentAd.getImages();

        if (images.size() > 0) {
            ((ImageView) adView.getImageView()).setImageDrawable(images.get(0).getDrawable());
        }

        // Some aren't guaranteed, however, and should be checked.
        NativeAd.Image logoImage = nativeContentAd.getLogo();

        if (logoImage == null) {
            adView.getLogoView().setVisibility(View.INVISIBLE);
        } else {
            ((ImageView) adView.getLogoView()).setImageDrawable(logoImage.getDrawable());
            adView.getLogoView().setVisibility(View.VISIBLE);
        }

        // Assign native ad object to the native view.
        adView.setNativeAd(nativeContentAd);
    }
}
