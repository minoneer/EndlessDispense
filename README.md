# EndlessDispense

A [Paper](https://papermc.io/) plugin for infinite dispensers and droppers.

EndlessDispense allows server owners and moderators to create dispensers and droppers which never run out of stock. This
is especially useful for drop events, distributing free items, adventures, etc.

In contrast to many other plugins of its kind, the plugin preserves all vanilla functions of droppers and dispensers,
including:

* Support for all items, including those with special tags, properties, or custom nbt data
* Different items for each slot
* Special dispense behavior, e.g., the shooting of projectiles

Developed for and used on [Minecraft Asylum](https://minecraft-asylum.de/), a German survival server since 2012.

## Usage

Setting up dispensers with infinite supply is easy.

1. Fill the dispenser or dropper as you would normally.
2. Look at the block and type `/supply on`.

That's it. You can now activate the dispenser with normal means, e.g., a button. When in endless mode, the inventory is protected and cannot be opened by unauthorized players.

To disable the endless mode, look at the block and type `/supply off`.

### Legacy Support

If you are upgrading from an older version of EndlessDispense, your existing sign-based setups can be migrated. Migration is off by default: set `migrate_legacy: true` in `plugins/EndlessDispense/config.yml` and restart the server. The plugin then migrates each sign-based dispenser to the new internal data system and removes the sign the first time it is interacted with (dispensing, opening inventory, or attempting to break). While migration is off, sign-based dispensers behave like normal dispensers.

## Commands & Permissions

| Command       | Description                                             | Permission               |
|---------------|---------------------------------------------------------|--------------------------|
| `/supply on`  | Enables endless mode for the block you are looking at.  | `endlessdispense.create` |
| `/supply off` | Disables endless mode for the block you are looking at. | `endlessdispense.create` |

* **`endlessdispense.create`**: Allows creating and toggling endless dispensers.
* **`endlessdispense.destroy`**: Allows breaking endless dispensers.

## For Developers

Contributions are welcome and encouraged. The plugin uses the maven build system, so getting set up
is simple:

1. Clone the repository: `git clone https://github.com/minoneer/EndlessDispense`
2. Build the final jar: `mvn package`

The build artifact will be in `target/EndlessDispense.jar`

Feel free to reach out if you have any concerns or wish to discuss potential contributions.

## Feature Requests and Bugs

Please create an issue to report any problems or request new features.
