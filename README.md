# SafeGuard

<ol >
<img src="https://github.com/eekowal/Safeguard/blob/master/screenshots/screenshot--5.jpg" width="188"/>
<img src="https://github.com/eekowal/Safeguard/blob/master/screenshots/screenshot--2.jpg" width="188"/>
<img src="https://github.com/eekowal/Safeguard/blob/master/screenshots/screenshot--3.jpg" width="188"/>
<img src="https://github.com/eekowal/Safeguard/blob/master/screenshots/screenshot--4.jpg" width="188"/>
</ol>

[SafeGuard: APK](https://github.com/eekowal/Safeguard/blob/master/safeguard-debug.apk)

[SafeGuard: Demo Video #1](https://vimeo.com/217745646)

[SafeGuard: Demo Video #2](https://vimeo.com/217745744)


**Note**: As described in the paper, the RSS feed in the bottom navigation bar is not implemented, and will always show an empty feed. The crime and weather views are implemented and live. 


## Testing the map server
Below are some example urls that you can use in your browser to view the overlays served by our tile server. 

### All crime data
http://35.185.45.122/crime

### Crime data filtered by type and/or time os day

http://35.185.45.122/crime/assault

http://35.185.45.122/crime/assault/day

http://35.185.45.122/crime/assault/night

### Changing the zoom level
http://35.185.45.122/crime/#14/38.8846/-76.8425

### Single tiles
http://35.185.45.122/crime/12/1173/1564@2x.png

