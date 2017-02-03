from django import forms

from main.fields import RegexField


class IndexForm(forms.Form):
    id_list = forms.CharField(
        widget=forms.Textarea, label='ID List',
        help_text='List of students IDs to query, one per line.')
    student_id_regex = RegexField(
        label='Student ID regex',
        help_text='Regular expression used to match the student ID in each '
                  'line. If cannot match (or a student is not found in the '
                  'database), then the line is left as is.',
        initial=r'\b\d{7,}\b',
        widget=forms.TextInput(attrs={'placeholder': r'\b\d{7,}\b'}))
