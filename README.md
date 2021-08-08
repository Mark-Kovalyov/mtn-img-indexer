# Img-Indexer

## Primary Goals:
* Off-line thumnails generator
* Pure filesystem as datasource
* EXIF tags to provide Date-Time based indexing
* JPEG image support
* Java-language based
* No active server scipting (JSP/ASPX/PHP)

## Secondary Goals
* Introduce Dagger-2 as DI-framework
* Re-compile with Graal compiler to achieve perfect warm-up time
* Investigate vector graphics abilities to achieve 

## Usefull java libs
Genetic
* API https://jenetics.io/javadoc/jenetics/6.2/index.html
* User Guide https://jenetics.io/manual/manual-6.2.0.pdf

## Team members

## JPEG Exif attributes

Sample for Nokia
```
--------------------+----------------------------------------------------------
Tag                 |Value
--------------------+----------------------------------------------------------
Manufacturer        |Nokia
Model               |205
Orientation         |Top-left
X-Resolution        |72
Y-Resolution        |72
Resolution Unit     |Inch
Software            |unknown
YCbCr Positioning   |Centered
Compression         |JPEG compression
X-Resolution        |72
Y-Resolution        |72
Resolution Unit     |Inch
Exif Version        |Exif Version 2.2
Components Configura|Y Cb Cr -
FlashPixVersion     |FlashPix Version 1.0
Color Space         |sRGB
Pixel X Dimension   |640
Pixel Y Dimension   |480
Custom Rendered     |Normal process
Exposure Mode       |Auto exposure
White Balance       |Auto white balance
Scene Capture Type  |Standard
Digital Zoom Ratio  |1.000
Date and Time (Origi|2016:08:14 19:47:45
Date and Time (Digit|2016:08:14 19:47:45
--------------------+----------------------------------------------------------
EXIF data contains a thumbnail (14467 bytes).

```

Sample for CanonPowerShot a510
```
exif IMG_0061.jpg 
EXIF tags in 'IMG_0061.jpg' ('Intel' byte order):
--------------------+----------------------------------------------------------
Tag                 |Value
--------------------+----------------------------------------------------------
Manufacturer        |Canon
Model               |Canon PowerShot A510
Orientation         |Top-left
X-Resolution        |180
Y-Resolution        |180
Resolution Unit     |Inch
Date and Time       |2005:08:15 16:32:28
YCbCr Positioning   |Centered
Compression         |JPEG compression
X-Resolution        |180
Y-Resolution        |180
Resolution Unit     |Inch
Exposure Time       |1/60 sec.
F-Number            |f/5.0
Exif Version        |Exif Version 2.2
Date and Time (Origi|2005:08:15 16:32:28
Date and Time (Digit|2005:08:15 16:32:28
Components Configura|Y Cb Cr -
Compressed Bits per | 5
Shutter Speed       |5.91 EV (1/59 sec.)
Aperture            |4.66 EV (f/5.0)
Exposure Bias       |0.00 EV
Maximum Aperture Val|4.66 EV (f/5.0)
Metering Mode       |Pattern
Flash               |Flash fired, auto mode
Focal Length        |18.7 mm
Maker Note          |894 bytes undefined data
User Comment        |
FlashPixVersion     |FlashPix Version 1.0
Color Space         |sRGB
Pixel X Dimension   |2048
Pixel Y Dimension   |1536
Focal Plane X-Resolu|9142.857
Focal Plane Y-Resolu|9142.857
Focal Plane Resoluti|Inch
Sensing Method      |One-chip color area sensor
File Source         |DSC
Custom Rendered     |Normal process
Exposure Mode       |Auto exposure
White Balance       |Auto white balance
Digital Zoom Ratio  |1.0000
Scene Capture Type  |Standard
Interoperability Ind|R98
Interoperability Ver|0100
RelatedImageWidth   |2048
RelatedImageLength  |1536
--------------------+----------------------------------------------------------
EXIF data contains a thumbnail (4807 bytes).
```

## AWS s3 md5 attribute
```
aws 
```

## XATTR/Attr

```bash
$ attr -l git.c
Attribute "xattr-foo" has a 11 byte value for git.c

$ attr -s key01 -V value01 git.c
Attribute "key01" set to a 7 byte value for git.c:
value01

$ attr -l git.c
Attribute "key01" has a 7 byte value for git.c
Attribute "xattr-foo" has a 11 byte value for git.c
```

## Usefull links from Leonid
* https://www.mallenom.ru/company/publications/321/
