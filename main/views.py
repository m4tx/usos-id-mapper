from django.views.generic import FormView

from main.forms import IndexForm


class IndexView(FormView):
    template_name = 'index.html'
    form_class = IndexForm
