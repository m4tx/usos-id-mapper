from django import forms

from main.fields import RegexField


class IndexForm(forms.Form):
    id_list = forms.CharField(
        help_text='List of students IDs to query, one per line.',
        label='ID List',
        widget=forms.Textarea(attrs={
            'placeholder': 'Random text\n1234567\n7654321'}))
    student_id_regex = RegexField(
        label='Student ID regex',
        help_text='Regular expression used to match the student ID in each '
                  'line. If cannot match (or a student is not found in the '
                  'database), then the line is left as is.',
        initial=r'\b\d{7,}\b',
        widget=forms.TextInput(attrs={'placeholder': r'\b\d{7,}\b'}))


class UploadFileForm(forms.Form):
    file = forms.FileField(help_text='Choose the PDF with tabular data')
    output_format = forms.ChoiceField(
        choices=[('ods', 'ODS'), ('xlsx', 'XLSX'), ('csv', 'CSV')],
        initial='csv', label='Output file format', widget=forms.RadioSelect)
