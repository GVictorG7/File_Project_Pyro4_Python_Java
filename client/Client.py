import json
import sys

import Pyro4


def print_files(files):
    for file in files:
        print("NUME: " + file["nume"] + " PATH: " + file["path"] + " HASH: " + file["hash"])
    print("\n")


class Client:
    def __init__(self, url_server):
        self.proxy = Pyro4.Proxy(url_server)
        print("Client Python Pyro4: " + url_server)

    def find_all_files(self):
        files = json.loads(self.proxy.find_all_files())
        print_files(files)

    def find_files_containing_substring(self):
        substring = input("Substring: ")
        files = json.loads(self.proxy.find_files_containing_substring(substring))
        print_files(files)

    def find_files_by_content_parts_text(self):
        content = input("Content (text): ")
        files = json.loads(self.proxy.find_files_by_content_parts_text(content))
        print_files(files)

    def find_files_by_content_parts_binary(self):
        content = input("Content (binary): ")
        files = json.loads(self.proxy.find_files_by_content_parts_binary(content))
        print_files(files)

    def find_files_with_duplicate_hash(self):
        files = json.loads(self.proxy.find_files_with_duplicate_hash())
        print_files(files)

    def menu(self, menu):
        for key in sorted(menu.keys()):
            print("\t" + key + ":  " + menu[key][0])

        try:
            choice = input("Option: ")
            _function = menu.get(str(choice))
            if isinstance(_function[1], dict):
                self.menu(_function[1])
            else:
                if _function[0] == "Back":
                    return
                _function[1]()
        except SystemExit:
            raise
        except (TypeError, NameError):
            print("Invalid option\n")

        self.menu(menu)

    def stop(self):
        raise SystemExit

    def start(self):
        content_type_menu = {"1": ("Text: ", self.find_files_by_content_parts_text),
                             "2": ("Binary: ", self.find_files_by_content_parts_binary),
                             "3": ("Back", None)
                             }

        menu = {"1": ("Find all files", self.find_all_files),
                "2": ("Find files containing substring", self.find_files_containing_substring),
                "3": ("Find files by parts of content", content_type_menu),
                "4": ("Find files with duplicate content", self.find_files_with_duplicate_hash)
                }

        self.menu(menu)
        self.start()


if __name__ == "__main__":
    if len(sys.argv) > 1:
        client = Client(sys.argv[1])
    else:
        client = Client("PYRO:exec@localhost:7543")

    client.start()
