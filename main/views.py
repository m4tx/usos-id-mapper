import re
from tempfile import NamedTemporaryFile

import django_excel
import pyexcel as pyexcel
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
        return self.handle_uploaded_file(form.files['file'])

    @staticmethod
    def handle_uploaded_file(file):
        api = API()
        with NamedTemporaryFile(suffix='.pdf', mode='wb') as pdf_file, \
                NamedTemporaryFile(suffix='.csv', mode='r') as csv_file:
            # Convert PDF to CSV
            for chunk in file.chunks():
                pdf_file.write(chunk)
            convert_into(pdf_file.name, csv_file.name, spreadsheet=True,
                         output_format='csv')

            # Iterate over the rows and if id student matched then use
            # get_student method to get their name
            prog = re.compile(r'\d{7,}')
            sheet = pyexcel.get_sheet(file_name=csv_file.name)
            for row in sheet.array:
                for cell in row:
                    if prog.match(str(cell)):
                        student = api.get_student(str(cell).strip())
                        if student is not None:
                            row += [student['first_name'], student['last_name']]
                            break

            # Output file
            return django_excel.make_response(sheet, 'csv', file_name='output')
