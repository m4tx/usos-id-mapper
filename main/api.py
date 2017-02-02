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

    def process_id_list(self, id_list: str):
        rv = []
        for student_id in id_list.splitlines():
            student = cache.get(student_id)
            if student is None:
                student = self.get_student(student_id)
                cache.set(student_id, student, None)
            if student is None:
                rv.append(student_id)
            else:
                rv.append('{}\t{}'.format(student['first_name'],
                                          student['last_name']))
        return '\n'.join(rv)
