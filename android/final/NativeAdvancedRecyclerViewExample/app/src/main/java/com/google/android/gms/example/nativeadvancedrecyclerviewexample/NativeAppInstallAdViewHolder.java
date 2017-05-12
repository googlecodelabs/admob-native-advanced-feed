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

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.google.android.gms.ads.formats.MediaView;
import com.google.android.gms.ads.formats.NativeAppInstallAdView;

public class NativeAppInstallAdViewHolder extends RecyclerView.ViewHolder {
    NativeAppInstallAdViewHolder(View view) {
        super(view);
        NativeAppInstallAdView adView = (NativeAppInstallAdView) view;

        // The MediaView will display a video asset if one is present in the ad, and the
        // first image asset otherwise.
        MediaView mediaView = (MediaView) adView.findViewById(R.id.appinstall_media);
        adView.setMediaView(mediaView);

        // Register the view used for each individual asset.
        adView.setHeadlineView(adView.findViewById(R.id.appinstall_headline));
        adView.setBodyView(adView.findViewById(R.id.appinstall_body));
        adView.setCallToActionView(adView.findViewById(R.id.appinstall_call_to_action));
        adView.setIconView(adView.findViewById(R.id.appinstall_app_icon));
        adView.setPriceView(adView.findViewById(R.id.appinstall_price));
        adView.setStarRatingView(adView.findViewById(R.id.appinstall_stars));
        adView.setStoreView(adView.findViewById(R.id.appinstall_store));
    }
}
