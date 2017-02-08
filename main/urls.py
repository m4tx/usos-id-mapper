from django.conf.urls import url

from main.views import IndexView, upload_file

urlpatterns = [
    url('^$', IndexView.as_view(), name='index'),
    url('^pdf$', upload_file, name='upload_file')
]
