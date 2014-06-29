Android Icon Generator (AIG)
======================

Tool used to generate icons for [Android Action Bar Icons](https://github.com/svenkapudija/Android-Action-Bar-Icons).

Currently supports only Action Bar Icon style (in all densities and in both `holo_dark` and `holo_light`).
Planned support for notification/status-bar and small/contextual icons.
Planned support for custom styles/themes (color and opacity).

See more at [Android Design - Iconography](http://developer.android.com/design/style/iconography.html).

### Options

    usage: aig inputFile/fontAwesomeIcon [options]
     -o,--output <file>              use given directory as image output
     -t,--themes <theme1,theme2...>  use given Android theme(s)
                                     -hl,--holo-light
                                     -hd,--holo-dark
                                     If none is provided, all themes are
                                     included.
     -d,--densities <dpi1, dpi2,...> use specific densities (dpi suffix is
                                     optional). If none is provided, all densities
                                     are included.
     -ab,--action-bar                generate action bar icon type (default)
     -b,--bundle                     put every icon into own folder
     -ldpi                           generate ldpi density
     -mdpi                           generate mdpi density
     -hdpi                           generate hdpi density
     -xhdpi                          generate xhdpi density
     -xxhdpi                         generate xxhdpi density
     -xxxhdpi                        generate xxxhdpi density
     -u,--update                     update FontAwesome icons
     -h,--help                       help
    
Add your `aig.jar` to `PATH` so you can use `aig` syntax or use `java -jar aig.jar` instead.
    
### Generate icon from file
    
    // Specify input image file path
    aig myImage.png

### Generate icon from FontAwesome icon

    // Specify FontAwesome icon identifier instead of input file
    aig fa-car

### Output directory

    // User home directory is used
    aig fa-car
    
    // Input file directory is used
    aig myImage.png
    
    // Specify output directory
    aig fa-car -o myDir/outputDir
    aig myImage.png -output myDir/outputDir

### Bundle

    // Output will be /drawable-hdpi/icon_fa_car.png
    aig fa-car
    
    // Output will be /icon_fa_car/drawable-hdpi/icon_fa_car.png
    aig fa-car -b

### Density

    // All 6 densities are created
    aig myImage.png
    
    // Only xxhdpi and xhdpi versions are created
    aig myImage.png -xxhdpi -xhdpi

### Themes

    // Both holo_light and holo_dark are created
    // each into own folder
    aig myImage.png
    
    // Only holo_light is created
    // No extra folder is created like in previous example
    aig myImage.png -t hl
    aig myImage.png --themes holo-light
    
### FontAwesome files are out-dated

    // Update them via
    aig -u
    aig --update



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
