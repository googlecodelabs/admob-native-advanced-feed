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

class TableViewController: UITableViewController {

  // MARK: - Properties

  // The table view items.
  var tableViewItems = [AnyObject]()

  // MARK: - UIViewController methods

  override func viewDidLoad() {
    super.viewDidLoad()
    tableView.register(UINib(nibName: "MenuItem", bundle: nil),
        forCellReuseIdentifier: "MenuItemViewCell")
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

    let menuItem = tableViewItems[indexPath.row] as? MenuItem

    let reusableMenuItemCell = tableView.dequeueReusableCell(withIdentifier: "MenuItemViewCell",
        for: indexPath) as! MenuItemViewCell

    reusableMenuItemCell.nameLabel.text = menuItem?.name
    reusableMenuItemCell.descriptionLabel.text = menuItem?.description
    reusableMenuItemCell.priceLabel.text = menuItem?.price
    reusableMenuItemCell.categoryLabel.text = menuItem?.category
    reusableMenuItemCell.photoView.image = menuItem?.photo

    return reusableMenuItemCell
  }

}
