from django.conf.urls import url

from main.views import IndexView, ProcessPDFView

urlpatterns = [
    url('^$', IndexView.as_view(), name='index'),
    url('^pdf/$', ProcessPDFView.as_view(), name='process_pdf')
]
