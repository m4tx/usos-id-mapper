import re

from django import forms
from django.core.exceptions import ValidationError


def validate_regex(value):
    try:
        re.compile(value)
    except re.error:
        raise ValidationError('{} is not a valid regular expression.'
                              .format(value))


class RegexField(forms.CharField):
    default_validators = [validate_regex]
