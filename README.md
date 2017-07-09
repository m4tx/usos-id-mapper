# usos-id-mapper

Simple webapplication that can map given list of student IDs to a list of their
names using USOS (Polish: Uniwersytecki System Obsługi Studiów) API.

## Installation
For local installation:

Make sure you have git, Python 3.5+ and Java (JRE) installed. Then execute the
following commands:
```sh
git clone https://github.com/m4tx/usos-id-mapper.git
cd usos-id-mapper/usosidmapper/settings
cp local_settings.py.example local_settings.py
# Fill out USOS_CONSUMER_KEY and USOS_CONSUMER_SECRET
$EDITOR local_settings.py
cd ../..
pip install -r requirements.txt
python manage.py runserver
```
