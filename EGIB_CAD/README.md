This is a README file for EGIB_CAD Java window app.
Contact me at: marfen <at> interia.pl
Status: Unreleased, used to streamline personal tasks in architecture
line of work.
What it does: This Java SWING app is used to transform coordinates 
from e-mapa services to usable prompts in AutoCAD to print a map.

More info and user manual:
The goverment website e-mapa allows to download coordinates of each
point in the border of selected (see procedure below) plot of land.
For some reason however - land surveyors in Poland use inverted axes
for points in space (X and Y -> Y axis is east-west direction, X axis
is north-south direction) which results in mirrored and rotated plots
if transferred to AutoCAD software in order to draw a concept or a 
masterplan. This app takes the output of e-mapa (which is downloadable
in the form of .txt file) and swaps coordinates adding prompts and
minor formatting to make a usable AutoCAD command.

Procedure of procurring coordinates:
1. Visit https://polska.e-mapa.net/
2. Find desired plot of land
3. Pick "arrow"/"picker" tool (Spacebar)
4. Select desired plot of land
5. Pick "marker" tool
6. Pick "Utworz z aktywnej dzialki" option in popup window
7. Enter value "0" when prompted
8. Pick "Dzialania" and "Wspolrzedne do pliku"
9. .txt file with coordinates download will start