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

class ViewController: UIViewController, GADNativeAppInstallAdLoaderDelegate,
    GADNativeContentAdLoaderDelegate {

  // MARK: - Properties

  /// The table view items.
  var tableViewItems = [AnyObject]()

  /// The ad unit ID from the AdMob UI.
  let adUnitID = "ca-app-pub-3940256099942544/8407707713"

  /// The number of native ads to load.
  let numAdsToLoad = 5

  /// The native ads.
  var nativeAds = [GADNativeAd]()

  /// The ad loader that loads the native ads.
  var adLoader: GADAdLoader!

  /// The number of completed ad loads (success or failures).
  var numAdLoadCallbacks = 0

  /// The spinner view.
  @IBOutlet weak var spinnerView: UIActivityIndicatorView!

  /// The show menu button.
  @IBOutlet weak var showMenuButton: UIButton!

  override func viewDidLoad() {
    super.viewDidLoad()

    // Load the menu items.
    addMenuItems()

    // Prepare the ad loader and start loading ads.
    adLoader = GADAdLoader(adUnitID: adUnitID,
                           rootViewController: self,
                           adTypes: [kGADAdLoaderAdTypeNativeAppInstall,
                                     kGADAdLoaderAdTypeNativeContent],
                           options: nil)
    adLoader.delegate = self
    preloadNextAd()
  }

  @IBAction func showMenu(_ sender: Any) {
    let tableVC = storyboard?.instantiateViewController(withIdentifier: "TableViewController")
        as! TableViewController
    tableVC.tableViewItems = tableViewItems
    self.present(tableVC, animated: true, completion: nil)
  }

  /// Preload native ads sequentially.
  func preloadNextAd() {
    if numAdLoadCallbacks < numAdsToLoad {
      adLoader.load(GADRequest())
    } else {
      addNativeAds()
      enableMenuButton()
    }
  }

  func enableMenuButton() {
    spinnerView.stopAnimating()
    showMenuButton.isEnabled = true
  }

  /// Adds MenuItems to the tableViewItems list.
  func addMenuItems() {
    var JSONObject: Any

    guard let path = Bundle.main.url(forResource: "menuItemsJSON", withExtension: "json") else {
      print("Invalid filename for JSON menu item data.")
      return
    }

    do {
      let data = try Data(contentsOf: path)
      JSONObject = try JSONSerialization.jsonObject(with: data,
                                                    options: JSONSerialization.ReadingOptions())
    } catch {
      print("Failed to load menu item JSON data: %s", error)
      return
    }

    guard let JSONObjectArray = JSONObject as? [Any] else {
      print("Failed to cast JSONObject to [AnyObject]")
      return
    }

    for object in JSONObjectArray {
      guard let dict = object as? [String: Any],
        let menuIem = MenuItem(dictionary: dict) else {
          print("Failed to load menu item JSON data.")
          return
      }
      tableViewItems.append(menuIem)
    }
  }

  /// Add native ads to the tableViewItems list.
  func addNativeAds() {
    let adInterval = (tableViewItems.count / nativeAds.count) + 1
    var index = 0
    for nativeAd in nativeAds {
      if index < tableViewItems.count {
        tableViewItems.insert(nativeAd, at: index)
        index += adInterval
      } else {
        break
      }
    }
  }

  // MARK: - GADAdLoaderDelegate

  func adLoader(_ adLoader: GADAdLoader, didFailToReceiveAdWithError error: GADRequestError) {
    print("\(adLoader) failed with error: \(error.localizedDescription)")

    // Increment the number of ad load callbacks.
    numAdLoadCallbacks += 1

    // Load the next native ad.
    preloadNextAd()
  }

  func adLoader(_ adLoader: GADAdLoader, didReceive nativeAppInstallAd: GADNativeAppInstallAd) {
    print("Received native app install ad: \(nativeAppInstallAd)")

    // Increment the number of ad load callbacks.
    numAdLoadCallbacks += 1

    // Add the native ad to the list of native ads.
    nativeAds.append(nativeAppInstallAd)

    // Load the next native ad.
    preloadNextAd()
  }

  func adLoader(_ adLoader: GADAdLoader, didReceive nativeContentAd: GADNativeContentAd) {
    print("Received native content ad: \(nativeContentAd)")

    // Increment the number of ad load callbacks.
    numAdLoadCallbacks += 1

    // Add the native ad to the list of native ads.
    nativeAds.append(nativeContentAd)

    // Load the next native ad.
    preloadNextAd()
  }

}
