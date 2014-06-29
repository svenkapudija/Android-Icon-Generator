Android Icon Generator
======================

Tool used to generate icons for [Android Action Bar Icons](https://github.com/svenkapudija/Android-Action-Bar-Icons).

Currently supports only Action Bar Icon style (in all densities and in both `holo_dark` and `holo_light`).
Planned support for notification/status-bar and small/contextual icons.
Planned support for custom styles/themes (color and opacity).

See more at [Android Design - Iconography](http://developer.android.com/design/style/iconography.html).

### Options

    usage: aig [options]
     -i,--input <file>               use given file or directory as image input
     -fa,--font-awesome <icon>       use given FontAwesome icon identifier as
                                     input (ex. fa-car)
     -o,--output <file>              use given directory as image output
     -t,--theme <theme1,theme2...>   use given Android theme(s)
                                     -hl,--holo-light
                                     -hd,--holo-dark
                                     If none is provided, all themes are
                                     included.
     -ab,--action-bar                generate action bar icon type (default)
     -ldpi                           generate ldpi density
     -mdpi                           generate mdpi density
     -hdpi                           generate hdpi density
     -xhdpi                          generate xhdpi density
     -xxhdpi                         generate xxhdpi density
     -xxxhdpi                        generate xxxhdpi density
     -u,--update                     update FontAwesome icons
     -h,--help                       help
    
### Generate icon from file
    
    // Specify input image file path
    java -jar aig.jar -i myImage.png
    java -jar aig.jar --input myImage.png

### Generate icon from FontAwesome icon

    // Specify FontAwesome icon identifier
    java -jar aig.jar -fa fa-car
    java -jar aig.jar --font-awesome fa-car

### Output directory

    // User home directory is used
    java -jar aig.jar -fa fa-car
    
    // Input file directory is used
    java -jar aig.jar -i myImage.png
    
    // Specify output directory
    java -jar aig.jar -fa fa-car -o myDir/outputDir
    java -jar aig.jar -i myImage.png -output myDir/outputDir

### Density

    // All 6 densities are created
    java -jar aig.jar -i myImage.png
    
    // Only xxhdpi and xhdpi versions are created
    java -jar aig.jar -i myImage.png -xxhdpi -xhdpi
    
### Themes

    // Both holo_light and holo_dark are created
    java -jar aig.jar -i myImage.png
    
    // Only holo_light is created
    java -jar aig.jar -i myImage.png -t hl
    or
    java -jar aig.jar -i myImage.png -t holo-light
    
### FontAwesome files are out-dated

    // Update them via
    java -jar aig.jar --update



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
