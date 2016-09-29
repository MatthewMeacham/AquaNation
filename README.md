# AquaNation

* *Authors* : Matthew Meacham, Nick Wilson, Lavanya Uppala, Trevor Holmes, Neil Band 
* *Technologies* : Java 7, JavaFX, Javax Swing
* *Summary* : A Java application for storing data on water samples collected across the United States.

## Purpose of this application

This application was created for a University of Nebraska - Lincoln professor and his graduate students. He wanted to be able to store information related to different contaminants found in water across the United States and visually see them on the states themselves. AquaNation enables them to input values and have certain value ranges translated to colors which fill in the states. This enables them to visualize, on the macro scale, trends happening across different regions in the United States. 

##Main Features

* Clickable map image
* Exporting as images, PDF, or spreadsheet
* Color ranges
* Keeps history of data entered
* Undo/Redo
* Save functionality
* Displays data in various graphs
* Statistic calculations
* Map slideshow
* Print map image
* Help menu
* Self-Updating


### Key Features Explained

This application continually stores values in order to observe, on the micro level, trends happening within the states themselves. It also can display these data in a bar graph, line graph, box plot, histogram, or data table. It also provides core statistics such as mean, median, sum of all values, minimum, maximum, etc.

In addition to keeping track of information, tools were added for presentations. A person can observe the changes through time through a slideshow interface with variable speeds for use when presenting the collected data. 

For easy transmission, the entire project can be saved and loaded from within the application. Also, it has the capabilities to print the map and to save the map as a .png, .jpg, .bmp. Additionally, the data can be exported as a .csv or in a regular .txt file for data analysis on paper.

For ease of fixing bugs, AquaNation has a built-in updating feature that can update itself if necessary by querying for the most recent version. If it needs updating, it will update itself and restart.

The entire application has unit and integration tests to assist with regression testing and ensure it is all working correctly.

Finally, for ease of use, a complete help section was added to the application so that users could easily figure out the application if there was any questions.

##Known Bugs and Enhancements to be made

* The tables don't allow you to click out once you've clicked in
* Change default colors from rainbow to soft blue or green progressions
* Add a custom legend to the map and all exports
* Add a custom title to the map and all exports
* Removes the lines that point to the states when a person chooses to remove both the state names and values
* Remove transparent background and instead make it like a solid white or a soft gray color
* "Saved data doesn't make new maps" - Numbers and colors and state names won't show on the map after loading a file even though the data is there
* Fix box plot, the points are just slightly off
* Add capability for "No Data" option in the color parameters, so they can select a color when there is no value
* Add capability for "No Data" point on graphs
* Allow users to delete columns
* When values are turned on, New Hampshire's value gets cut off