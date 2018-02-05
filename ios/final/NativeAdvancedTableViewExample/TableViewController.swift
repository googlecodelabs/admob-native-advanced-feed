//
//  Copyright (C) 2017 Google, Inc.
//
//  Licensed under the Apache License, Version 2.0 (the "License");
//  you may not use this file except in compliance with the License.
//  You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
//  Unless required by applicable law or agreed to in writing, software
//  distributed under the License is distributed on an "AS IS" BASIS,
//  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//  See the License for the specific language governing permissions and
//  limitations under the License.
//

import Firebase
import UIKit

class TableViewController: UITableViewController {

  // MARK: - Properties

  /// The table view items.
  var tableViewItems = [AnyObject]()

  // MARK: - UIViewController methods

  override func viewDidLoad() {
    super.viewDidLoad()
    tableView.register(UINib(nibName: "MenuItem", bundle: nil),
        forCellReuseIdentifier: "MenuItemViewCell")
    tableView.register(UINib(nibName: "UnifiedNativeAdCell", bundle: nil),
        forCellReuseIdentifier: "UnifiedNativeAdCell")
  }

  // MARK: - UITableView delegate methods

  override func numberOfSections(in tableView: UITableView) -> Int {
    return 1
  }

  override func tableView(_ tableView: UITableView,
      heightForRowAt indexPath: IndexPath) -> CGFloat {
    return 120
  }

  override func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
    return tableViewItems.count
  }

  override func tableView(_ tableView: UITableView,
      cellForRowAt indexPath: IndexPath) -> UITableViewCell {

    if let menuItem = tableViewItems[indexPath.row] as? MenuItem {

      let reusableMenuItemCell = tableView.dequeueReusableCell(withIdentifier: "MenuItemViewCell",
          for: indexPath) as! MenuItemViewCell

      reusableMenuItemCell.nameLabel.text = menuItem.name
      reusableMenuItemCell.descriptionLabel.text = menuItem.description
      reusableMenuItemCell.priceLabel.text = menuItem.price
      reusableMenuItemCell.categoryLabel.text = menuItem.category
      reusableMenuItemCell.photoView.image = menuItem.photo

      return reusableMenuItemCell
    } else {
      let nativeAd = tableViewItems[indexPath.row] as! GADUnifiedNativeAd
      /// Set the native ad's rootViewController to the current view controller.
      nativeAd.rootViewController = self

      let nativeAdCell = tableView.dequeueReusableCell(
          withIdentifier: "UnifiedNativeAdCell", for: indexPath)

      // Get the ad view from the Cell. The view hierarchy for this cell is defined in
      // UnifiedNativeAdCell.xib.
      let adView : GADUnifiedNativeAdView = nativeAdCell.contentView.subviews
        .first as! GADUnifiedNativeAdView

      // Associate the ad view with the ad object.
      // This is required to make the ad clickable.
      adView.nativeAd = nativeAd

      // Populate the ad view with the ad assets.
      (adView.headlineView as! UILabel).text = nativeAd.headline
      (adView.priceView as! UILabel).text = nativeAd.price
      if let starRating = nativeAd.starRating {
        (adView.starRatingView as! UILabel).text =
            starRating.description + "\u{2605}"
      } else {
        (adView.starRatingView as! UILabel).text = nil
      }
      (adView.bodyView as! UILabel).text = nativeAd.body
      (adView.advertiserView as! UILabel).text = nativeAd.advertiser
      // The SDK automatically turns off user interaction for assets that are part of the ad, but
      // it is still good to be explicit.
      (adView.callToActionView as! UIButton).isUserInteractionEnabled = false
      (adView.callToActionView as! UIButton).setTitle(
          nativeAd.callToAction, for: UIControlState.normal)

      return nativeAdCell
    } 
  }

}
