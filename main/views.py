import csv
import re
import tempfile
from wsgiref.util import FileWrapper

from django.http import HttpResponse
from django.views.generic import FormView
from tabula import convert_into

from main.api import API
from main.forms import IndexForm, UploadFileForm


class IndexView(FormView):
    template_name = 'index.html'
    form_class = IndexForm

    def form_valid(self, form):
        api = API()
        id_list = form.cleaned_data['id_list']
        student_id_regex = form.cleaned_data['student_id_regex']
        new_id_list = api.process_id_list(id_list, student_id_regex)
        data = form.data.copy()
        data['id_list'] = new_id_list
        form.data = data
        return self.render_to_response(self.get_context_data(form=form))


class ProcessPDFView(FormView):
    template_name = 'process_pdf.html'
    form_class = UploadFileForm

    def form_valid(self, form):
        return handle_uploaded_file(form.files['file'])


def handle_uploaded_file(f):
    api = API()
    with tempfile.NamedTemporaryFile(suffix='.pdf', mode='wb') as pdf_file, \
            tempfile.NamedTemporaryFile(suffix='.csv', mode='r') as csv_file, \
            tempfile.NamedTemporaryFile(suffix='.csv', mode='w+') as output:
        for chunk in f.chunks():
            pdf_file.write(chunk)
        convert_into(pdf_file.name, csv_file.name, spreadsheet=True,
                     output_format="csv")
        # Iterate over all rows and if id student matched then use cache or
        # get_student method and cache
        student_id_regex = '\d{7,}'
        prog = re.compile(student_id_regex)
        reader = csv.reader(csv_file)
        writer = csv.writer(output, lineterminator='\n')
        for row in reader:
            match = list(filter(prog.match, row))
            res = row
            if match:
                student_id = match[0]
                student = api.get_student(student_id)
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
