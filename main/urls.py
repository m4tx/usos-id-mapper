from django.conf.urls import url

from main.views import ProcessTextView, ProcessPDFView

urlpatterns = [
    url('^$', ProcessPDFView.as_view(), name='process_pdf'),
    url('^text/$', ProcessTextView.as_view(), name='process_text'),
]
