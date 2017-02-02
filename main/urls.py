from django.conf.urls import url

from main.views import IndexView

urlpatterns = [
    url('^$', IndexView.as_view(), name='index')
]
