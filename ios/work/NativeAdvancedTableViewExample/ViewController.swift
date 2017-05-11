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

import UIKit

class ViewController: UIViewController {

  // The spinner view.
  @IBOutlet weak var spinnerView: UIActivityIndicatorView!

  // The show menu button.
  @IBOutlet weak var showMenuButton: UIButton!

  // The table view items.
  var tableViewItems = [AnyObject]()

  override func viewDidLoad() {
    super.viewDidLoad()

    // Load the menu items.
    addMenuItems()

    // Enable the menu.
    enableMenuButton()
  }

  @IBAction func showMenu(_ sender: Any) {
    let tableVC = storyboard?.instantiateViewController(withIdentifier: "TableViewController")
        as! TableViewController
    tableVC.tableViewItems = tableViewItems
    self.present(tableVC, animated: true, completion: nil)
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

}
