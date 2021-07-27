# Sheet
A shard that provides an easy-to-use api for saving and storing configurations.

## Usage
Sheet will handle all the saving and loading of data, so all you have to worry about is properly configuring it.

### Adding A Type
To add a type, you first need to register it, so that it will be properly deserialized whenever shard is loaded. To do this, you need a listener for the `sheet` hook, that looks something like this:
```java
package com.github.bottlemc.sheet;

import com.github.glassmc.loader.GlassLoader;
import com.github.glassmc.loader.Listener;

public class TestSheetListener implements Listener {

    @Override
    public void run() {
        GlassLoader.getInstance().getAPI(Sheet.class).registerType("testType", TestType.class);
    }

}
```

### Retrieving Data
```java
TestType testType = sheet.load("testType");
```

### Saving Data
```java
sheet.save("testType", testType);
```
> **Note:** This is technically not needed for objects, because all this does is modify the value that is stored in the map, which if it was already retrieved, is already present.