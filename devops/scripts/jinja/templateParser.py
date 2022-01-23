#!/usr/bin/python
import jinja2

data = {
    "name": "My Name",
    "iter": 5,
    "showHeader": False
}


def load_template():
    template_loader = jinja2.FileSystemLoader(searchpath="./")
    template_env = jinja2.Environment(loader=template_loader, trim_blocks=True)
    template = template_env.get_template("template.txt.jinja")
    print(template.render(data))


def main():
    load_template()


if __name__ == "__main__":
    main()
