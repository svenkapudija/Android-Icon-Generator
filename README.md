Android Icon Generator
======================

Tool used to generate icons for [Android Action Bar Icons](https://github.com/svenkapudija/Android-Action-Bar-Icons).

Currently supports only Action Bar Icon style (in all densities and in both `holo_dark` and `holo_light`).
Planned support for notification/status-bar and small/contextual icons.

See more at [Android Design - Iconography](http://developer.android.com/design/style/iconography.html).

Planned CLI support (so you can run the program `.jar` directly from the command line).

### How to generate icons?

    // Create new icon generator and specify the theme
    AndroidIconGenerator generator = new AndroidActionBarIconGenerator(AndroidTheme.HOLO_LIGHT);
    
    // Provide the image (from file in this example) and forward it to generator
    BufferedImage inputImage = ImageIO.read(new File("my_image.png"));
    BufferedImage outputImage = generator.generateIcon(inputImage, DIP.XXHDPI);
    
    // Save the image to file
    ImageIO.write(outputImage, "png", new FileOutputStream("fa-car.png"));
    
### Font Awesome support

You can directly generate icons just by providing Font Awesome icon identifier (for ex. `fa-car`).
Program will automatically download all the necessary `.ttf` files and unicode mappings.

    // Create FontAwesome image
    FontAwesome fontAwesome = new FontAwesome();
    BufferedImage image = fontAwesome.convertToImage("fa-car");
    
    // Forward image to generator
    AndroidIconGenerator generator = new AndroidActionBarIconGenerator(AndroidTheme.HOLO_LIGHT);
    BufferedImage outputImage = generator.generateIcon(image, DIP.XXHDPI);
    
    // Save the image to file
    ImageIO.write(outputImage, "png", new FileOutputStream("fa-car.png"));

Developed by
------------
* Sven Kapuđija

License
-------

    Copyright 2014 Sven Kapuđija
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
    http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
