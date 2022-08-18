# EndlessDispense

A [Spigot](https://www.spigotmc.org/) plugin for infinite dispensers and droppers via supply signs.

EndlessDispense allows server owners and moderators to create dispensers and droppers which never run out of stock. This
is especially useful for drop events, distributing free items, adventures, etc.

In contrast to many other plugins of its kind, the plugin preserves all vanilla functions of droppers and dispensers,
including:

* Support for all items, including those with special tags, properties, or custom nbt data
* Different items for each slot
* Special dispense behavior, e.g., the shooting of projectiles

Setting up dispensers with infinite supply is easy.

* Fill the dispenser or dropper as you would normally
* Attach a sign to any side, or place it below, with "supply" as the first line

That's it. You can now activate the dispenser with normal means, e.g., a button.

__For server owners:__ more information, installation instructions and completed builds are available on the plugin
page.

__For developers:__ contributions are welcome and encouraged. The plugin uses the maven build system, so getting set up
is simple:

1. Clone the repository: `git clone https://github.com/minoneer/EndlessDispense`

2. Build the final jar: `mvn package`

The build artifact will be in `target/EndlessDispense.jar`

Feel free to reach out if you have any concerns or wish to discuss potential contributions.

__Feature Requests and Bugs:__

Please create an issue to report any problems or request new features.
