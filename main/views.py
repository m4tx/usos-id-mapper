from tempfile import NamedTemporaryFile

import django_excel
import pyexcel
from django.views.generic import FormView
from os import path
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
        return self.handle_uploaded_file(form.files['file'],
                                         form.cleaned_data['output_format'])

    @staticmethod
    def handle_uploaded_file(file, output_format):
        api = API()
        with NamedTemporaryFile(suffix='.pdf', mode='wb') as pdf_file, \
                NamedTemporaryFile(suffix='.csv', mode='r') as csv_file:
            # Convert PDF to CSV
            for chunk in file.chunks():
                pdf_file.write(chunk)
            convert_into(pdf_file.name, csv_file.name, spreadsheet=True,
                         pages='all', output_format='csv')

            # Process
            sheet = pyexcel.get_sheet(file_name=csv_file.name)
            sheet.name = 'Output'
            api.process_spreadsheet(sheet)

            # Output file
            return django_excel.make_response(
                sheet, output_format,
                file_name=path.splitext(path.basename(file.name))[0])
