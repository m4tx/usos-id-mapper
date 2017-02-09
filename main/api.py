import re

from django.core.cache import cache
from usosapi import USOSAPIConnection


class API(USOSAPIConnection):
    def get_student(self, student_id):
        """Query the student_id in the USOS database or get it from cache

        :param str student_id: student ID to query
        :return: dictionary containing student's first and last name if they
            were found in the database; `None` otherwise
        :rtype: dict|None
        """
        cache_student = cache.get(student_id)
        if cache_student is not None:
            return cache_student

        data = {
            'lang': 'pl',
            'fields': 'items[user[first_name|last_name]]',
            'query': student_id,
            'num': '1'
        }
        response = self.get('services/users/search2', **data)
        if response['items']:
            student = response['items'][0]['user']
            cache.set(student_id, student)
            return student
        return None

    def process_id_list(self, id_list, student_id_regex):
        """Go through lines in id_list and replace student IDs with their names.

        :param str id_list: list of student IDs, one per line
        :param str student_id_regex: regex that matches student ID in each line.
            If the regex cannot be matched or a student is not found in the
            cache nor the database, then a line is left without changes.
        :return: processed string
        :rtype: str
        """
        prog = re.compile(student_id_regex)
        rv = []
        for line in id_list.splitlines():
            match = prog.match(line)
            if match is not None:
                student_id = match.group(0)
                student = self.get_student(student_id)
                if student is not None:
                    rv.append('{}\t{}'.format(student['first_name'],
                                              student['last_name']))
                    continue
            rv.append(line)
        return '\n'.join(rv)
