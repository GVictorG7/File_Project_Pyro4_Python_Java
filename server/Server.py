import json

import Pyro4
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

from repo.FileRepo import FileRepo
from service.FileService import FileService


@Pyro4.expose
class Server(object):
    def __init__(self, service):
        self.service = service

    def find_all_files(self):
        files = self.service.find_all_files()
        files_json = json.dumps(files, default=lambda x: x.to_dict())
        return files_json

    def find_files_containing_substring(self, substring):
        files = self.service.find_files_containing_substring(substring)
        files_json = json.dumps(files, default=lambda x: x.to_dict())
        return files_json

    def find_files_by_content_parts_text(self, content):
        files = self.service.find_files_by_content_parts_text(content)
        files_json = json.dumps(files, default=lambda x: x.to_dict())
        return files_json

    def find_files_by_content_parts_binary(self, content):
        files = self.service.find_files_by_content_parts_binary(content)
        files_json = json.dumps(files, default=lambda x: x.to_dict())
        return files_json

    def find_files_with_duplicate_hash(self):
        files = self.service.find_files_with_duplicate_hash()
        files_json = json.dumps(files, default=lambda x: x.to_dict())
        return files_json


def start():
    connection_string = 'mysql://root:root@localhost/fisiere'
    engine = create_engine(connection_string)
    session = sessionmaker(bind=engine)
    conn = engine.connect()
    session = session(bind=conn)

    file_repo = FileRepo(session)
    service = FileService(file_repo)

    daemon = Pyro4.Daemon(port=7543)
    uri = daemon.register(Server(service), "exec")
    print("Python Exec Pyro4 waiting with name \"exec\" at: " + str(uri))
    daemon.requestLoop()


if __name__ == "__main__":
    start()
