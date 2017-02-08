from django.conf import settings
from django.views.generic import FormView
from django.shortcuts import render
from django.http import HttpResponse
from django.core.cache import cache

from main.api import API
from main.forms import IndexForm, UploadFileForm

from tabula import convert_into
from wsgiref.util import FileWrapper
import csv
import re
import tempfile


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


def upload_file(request):
    if request.method == 'POST':
        form = UploadFileForm(request.POST, request.FILES)
        if form.is_valid():
            return handle_uploaded_file(request.FILES['file'])
    else:
        form = UploadFileForm()
    return render(request, 'upload.html', {'form': form})


def handle_uploaded_file(f):
    api = API(settings.USOS_URL, settings.USOS_CONSUMER_KEY,
              settings.USOS_CONSUMER_SECRET)
    with tempfile.NamedTemporaryFile(suffix='.pdf', mode='wb') as pdf_file,\
    	tempfile.NamedTemporaryFile(suffix='.csv', mode='r') as csv_file,\
    	tempfile.NamedTemporaryFile(suffix='.csv', mode='w+') as output:
        for chunk in f.chunks():
            pdf_file.write(chunk)
        convert_into(pdf_file.name, csv_file.name, spreadsheet=True, output_format="csv")
        '''iterate over all rows and if id student matched then use cache or get_student method and cache'''
        student_id_regex = '\d{7,}'
        prog = re.compile(student_id_regex)
        reader = csv.reader(csv_file)
        writer = csv.writer(output, lineterminator='\n')
        for row in reader:
            match = list(filter(prog.match, row))
            res = row
            if match:
                student_id = match[0]
                student = cache.get(student_id)
                if student is None:
                    student = api.get_student(student_id)
                    cache.set(student_id, student, None)
                if student is not None:
                    res.append(student['first_name'])
                    res.append(student['last_name'])
            writer.writerow(res)
        output.flush()
        output.seek(0)
        wrapper = FileWrapper(output)
        response = HttpResponse(wrapper, content_type='text/csv')
        response['Content-Disposition'] = 'attachment; filename="output.csv"'
    return response
