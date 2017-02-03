from django import forms


class IndexForm(forms.Form):
    usos_auth_pin = forms.IntegerField(
        label='USOS Authorization PIN',
        help_text='If not filled out, then only the cache is used. Note that '
                  'this means that some IDs may fail to be looked up.',
        required=False)
    id_list = forms.CharField(
        widget=forms.Textarea, label='ID List',
        help_text='List of students IDs to query, one per line.')
    student_id_regex = forms.CharField(
        label='Student ID regex',
        help_text='Regular expression used to match the student ID in each '
                  'line. If cannot match (or a student is not found in the '
                  'database), then the line is left as is.',
        initial=r'\b\d{7,}\b',
        widget=forms.TextInput(attrs={'placeholder': r'\b\d{7,}\b'}))
