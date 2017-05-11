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
    tableView.register(UINib(nibName: "NativeAppInstallAdCell", bundle: nil),
        forCellReuseIdentifier: "NativeAppInstallAdCell")
    tableView.register(UINib(nibName: "NativeContentAdCell", bundle: nil),
        forCellReuseIdentifier: "NativeContentAdCell")
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
    } else if let nativeAppInstallAd = tableViewItems[indexPath.row] as? GADNativeAppInstallAd {
      /// Set the native ad's rootViewController to the current view controller.
      nativeAppInstallAd.rootViewController = self

      let nativeAppInstallAdCell = tableView.dequeueReusableCell(
          withIdentifier: "NativeAppInstallAdCell", for: indexPath)

      // Get the app install ad view from the Cell. The view hierarchy for this cell is defined in
      // NativeAppInstallAdCell.xib.
      let appInstallAdView = nativeAppInstallAdCell.subviews[0].subviews[0]
          as! GADNativeAppInstallAdView

      // Associate the app install ad view with the app install ad object.
      // This is required to make the ad clickable.
      appInstallAdView.nativeAppInstallAd = nativeAppInstallAd

      // Populate the app install ad view with the app install ad assets.
      (appInstallAdView.headlineView as! UILabel).text = nativeAppInstallAd.headline
      (appInstallAdView.priceView as! UILabel).text = nativeAppInstallAd.price
      (appInstallAdView.starRatingView as! UILabel).text =
          nativeAppInstallAd.starRating!.description + "\u{2605}"
      (appInstallAdView.bodyView as! UILabel).text = nativeAppInstallAd.body
      // The SDK automatically turns off user interaction for assets that are part of the ad, but
      // it is still good to be explicit.
      (appInstallAdView.callToActionView as! UIButton).isUserInteractionEnabled = false
      (appInstallAdView.callToActionView as! UIButton).setTitle(
          nativeAppInstallAd.callToAction, for: UIControlState.normal)

      return nativeAppInstallAdCell
    } else {
      let nativeContentAd = tableViewItems[indexPath.row] as! GADNativeContentAd

      /// Set the native ad's rootViewController to the current view controller.
      nativeContentAd.rootViewController = self

      let nativeContentAdCell = tableView.dequeueReusableCell(
          withIdentifier: "NativeContentAdCell", for: indexPath)

      // Get the content ad view from the Cell. The view hierarchy for this cell is defined in
      // NativeContentAdCell.xib.
      let contentAdView = nativeContentAdCell.subviews[0].subviews[0]
        as! GADNativeContentAdView

      // Associate the content ad view with the content ad object.
      // This is required to make the ad clickable.
      contentAdView.nativeContentAd = nativeContentAd

      // Populate the content ad view with the content ad assets.
      (contentAdView.headlineView as! UILabel).text = nativeContentAd.headline
      (contentAdView.bodyView as! UILabel).text = nativeContentAd.body
      (contentAdView.advertiserView as! UILabel).text = nativeContentAd.advertiser
      // The SDK automatically turns off user interaction for assets that are part of the ad, but
      // it is still good to be explicit.
      (contentAdView.callToActionView as! UIButton).isUserInteractionEnabled = false
      (contentAdView.callToActionView as! UIButton).setTitle(
          nativeContentAd.callToAction, for: UIControlState.normal)

      return nativeContentAdCell
    }
  }

}
