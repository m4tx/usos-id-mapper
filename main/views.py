from django.conf import settings
from django.views.generic import FormView

from main.api import API
from main.forms import IndexForm


class IndexView(FormView):
    template_name = 'index.html'
    form_class = IndexForm

    def form_valid(self, form):
        api = API(settings.USOS_URL, settings.USOS_CONSUMER_KEY,
                  settings.USOS_CONSUMER_SECRET)
        id_list = form.cleaned_data['id_list']
        student_id_regex = form.cleaned_data['student_id_regex']
        new_id_list = api.process_id_list(id_list, student_id_regex)
        data = form.data.copy()
        data['id_list'] = new_id_list
        form.data = data
        return self.render_to_response(self.get_context_data(form=form))
