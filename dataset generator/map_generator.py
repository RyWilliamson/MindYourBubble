from uk_covid19 import Cov19API
import numpy as np
import json
import random
import os

data_filter = ['areaType=utla']

data_structure = {
    "date": "date",
    "areaName": "areaName",
    "areaCode": "areaCode",
    "newCasesByPublishDate": "newCasesByPublishDate",
    "cumCasesByPublishDateRate": "cumCasesByPublishDateRate",
}

api = Cov19API(filters=data_filter, structure=data_structure)

NHS_FOLDER = "nhs_data/"
FAKE_FOLDER = "fake_map_data/"


def setup_folders():
    if not os.path.exists(NHS_FOLDER):
        os.mkdir(NHS_FOLDER)
    if not os.path.exists(FAKE_FOLDER):
        os.mkdir(FAKE_FOLDER)


def read_or_get():
    try:
        with open(NHS_FOLDER + 'data.json', 'r') as f:
            data = json.load(f)
    except FileNotFoundError:
        data = api.get_json()
        scottish = extract_scottish(data)
        write_pretty_json(NHS_FOLDER + 'data.json', data)
        write_pretty_json(NHS_FOLDER + 'data_scots.json', scottish)
        return (data, scottish)

    try:
        with open(NHS_FOLDER + 'data_scots.json', 'r') as f:
            scottish = json.load(f)
    except FileNotFoundError:
        scottish = extract_scottish(data)
        write_pretty_json(NHS_FOLDER + 'data_scots.json', scottish)

    return (data, scottish)


def extract_scottish(data):
    scottish = {"data": []}
    for datum in data["data"]:
        if datum['areaCode'].startswith('S'):
            scottish["data"].append(datum)
    return scottish


def write_pretty_json(filename, data):
    with open(filename, 'w') as f:
        print(json.dumps(data, indent=4), file=f)


def extract_counties(data):
    counties = {}
    for datum in data["data"]:
        if datum["areaCode"] not in counties:
            counties[datum["areaCode"]] = {"areaName": datum["areaName"]}
    return counties


def generate_single_county_temps(days):
    average_temp = round(random.uniform(35.3, 38.5) * 10) / 10
    temp_variability = random.uniform(0.2, 0.6)
    temps = np.around(np.random.normal(
        loc=average_temp, scale=temp_variability, size=days), decimals=2)
    return temps.tolist()


def generate_all_temps(counties, days):
    for county in counties:
        counties[county]["temps"] = generate_single_county_temps(days)


setup_folders()
data, scottish = read_or_get()
counties = extract_counties(scottish)
generate_all_temps(counties, 28)
write_pretty_json(FAKE_FOLDER + 'map_temps.json', counties)
