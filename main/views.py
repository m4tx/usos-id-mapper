from django.conf import settings
from django.http import HttpResponse
from django.views.generic import FormView

from main.api import API
from main.forms import IndexForm


class IndexView(FormView):
    template_name = 'index.html'
    form_class = IndexForm

    def __init__(self):
        super(IndexView, self).__init__()
        self.api = None
        self.api_auth_url = None

    def get_initial(self):
        initial = super().get_initial()
        self.api = API(settings.USOS_URL, settings.USOS_CONSUMER_KEY,
                       settings.USOS_CONSUMER_SECRET)
        self.api_auth_url = self.api.get_authorization_url()
        self.request.session['request_token'] = self.api._request_token
        self.request.session[
            'request_token_secret'] = self.api._request_token_secret
        return initial

    def get_context_data(self, **kwargs):
        context = super().get_context_data(**kwargs)
        context['auth_url'] = self.api_auth_url
        return context

    def form_valid(self, form):
        self.api._request_token = self.request.session['request_token']
        self.api._request_token_secret = self.request.session[
            'request_token_secret']
        id_list = form.cleaned_data['id_list']
        new_id_list = self.api.process_id_list(id_list)
        data = form.data.copy()
        data['id_list'] = new_id_list
        form.data = data
        return self.render_to_response(self.get_context_data(form=form))
