import re

from django.core.cache import cache
from usosapi import USOSAPIConnection


class API(USOSAPIConnection):
    def get_student(self, student_id):
        data = {
            'lang': 'pl',
            'fields': 'items[user[first_name|last_name]]',
            'query': student_id,
            'num': '1'
        }
        response = self.get('services/users/search2', **data)
        if response['items']:
            return response['items'][0]['user']
        return None

    def process_id_list(self, id_list: str, student_id_regex: str):
        prog = re.compile(student_id_regex)
        rv = []
        for line in id_list.splitlines():
            match = prog.match(line)
            if match is not None:
                student_id = match.group(0)
                student = cache.get(student_id)
                if student is None:
                    student = self.get_student(student_id)
                    cache.set(student_id, student, None)
                if student is not None:
                    rv.append('{}\t{}'.format(student['first_name'],
                                              student['last_name']))
                    continue
            rv.append(line)
        return '\n'.join(rv)
