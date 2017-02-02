from django import forms


class IndexForm(forms.Form):
    usos_auth_pin = forms.IntegerField(label='USOS Authorization PIN')
    id_list = forms.CharField(
        widget=forms.Textarea, label='ID List',
        help_text='List of students IDs to query, one per line.')
