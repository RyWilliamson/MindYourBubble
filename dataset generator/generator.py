import sys
import datetime
import json
import random
import os
import string
import numpy as np

FOLDER = "people/"

if len(sys.argv) < 2:
    print("Not provided arguments")
    exit(1)


def readConfig(filename=sys.argv[1]):
    with open(filename, 'r') as f:
        data = json.load(f)
    return data


def setup_folders():
    if not os.path.exists(FOLDER):
        os.mkdir(FOLDER)


setup_folders()

configData = readConfig()

interval = configData["interval"]


def generateHeartRate(size, person_cfg, person_data, unit="BPM"):
    person_data["heart_rate"] = {}
    # In 30 minute intervals
    average_bpm = random.randint(
        person_cfg["heart_rate"]["average_lower"],
        person_cfg["heart_rate"]["average_upper"])

    variability = random.randint(
        person_cfg["heart_rate"]["variability_lower"],
        person_cfg["heart_rate"]["variability_upper"])

    heart_rates = np.around(np.random.normal(
        loc=average_bpm, scale=variability, size=size))

    person_data["heart_rate"]["unit"] = unit
    person_data["heart_rate"]["data"] = heart_rates.tolist()


def generateSkinTemp(size, person_cfg, person_data, unit="C"):
    person_data["skin_temp"] = {}
    average_temp = round(random.uniform(
        person_cfg["skin_temp"]["average_lower"],
        person_cfg["skin_temp"]["average_upper"]) * 10) / 10

    temp_variability = random.uniform(
        person_cfg["skin_temp"]["variability_lower"],
        person_cfg["skin_temp"]["variability_lower"])

    temps = np.around(np.random.normal(
        loc=average_temp, scale=temp_variability, size=size), decimals=2)

    person_data["skin_temp"]["unit"] = unit
    person_data["skin_temp"]["data"] = temps.tolist()


def generateSleepEpisode(size, person_cfg, person_data):
    person_data["sleep"] = {}
    person_data["sleep"]["data"] = []
    nights = (person_data["end_time"] - person_data["start_time"]).days

    # No of hours slept per night in total
    average_sleep = round(random.uniform(
        person_cfg["sleep"]["average_lower"],
        person_cfg["sleep"]["average_upper"]) * 2) / 2

    sleep_variability = random.uniform(
        person_cfg["sleep"]["variability_lower"],
        person_cfg["sleep"]["variability_upper"])

    sleeps = np.around(np.random.normal(
        loc=average_sleep, scale=sleep_variability, size=nights), decimals=1)

    # How long taken to fall asleep in minutes
    # Sometimes produces negative - should be taken as device malfunction
    average_sleep_onset = round(random.uniform(
        person_cfg["sleep"]["average_onset_lower"],
        person_cfg["sleep"]["average_onset_upper"]) * 2) / 2

    onset_variability = random.uniform(
        person_cfg["sleep"]["variability_onset_lower"],
        person_cfg["sleep"]["variability_onset_upper"])

    onsets = np.around(np.random.normal(
        loc=average_sleep_onset, scale=onset_variability, size=nights), decimals=1)

    # How many times they've woken during the night
    awakenings = np.random.randint(
        0, person_cfg["sleep"]["awakenings_upper"], nights)

    for i in range(nights):
        cur_dict = {}
        cur_dict["duration"] = sleeps[i]
        cur_dict["onset"] = onsets[i]
        cur_dict["awakenings"] = int(awakenings[i])
        person_data["sleep"]["data"].append(cur_dict)


def generateRespiratoryRate(size, person_cfg, person_data, unit="breaths/min"):
    person_data["respiratory_rate"] = {}
    average_rr = random.randint(
        person_cfg["respiratory_rate"]["average_lower"],
        person_cfg["respiratory_rate"]["average_upper"])

    rr_variability = random.randint(
        person_cfg["respiratory_rate"]["variability_lower"],
        person_cfg["respiratory_rate"]["variability_upper"])

    respiratory_rates = np.around(np.random.normal(
        loc=average_rr, scale=rr_variability, size=size))

    person_data["respiratory_rate"]["unit"] = unit
    person_data["respiratory_rate"]["data"] = respiratory_rates.tolist()


def generatePerson(person_cfg, postcode, household_name, other_connections):
    person_data = {}

    person_data["name"] = person_cfg["name"]
    person_data["postcode"] = postcode
    person_data["household"] = household_name
    person_data["username"] = person_cfg["username"]
    person_data["password"] = generatePassword(20)
    #start_time = datetime.datetime.strptime('Jun 1 2005  1:33PM', '%b %d %Y %I:%M%p')
    person_data["start_time"] = datetime.datetime.strptime(
        person_cfg["start_time"], '%b %d %Y %I:%M%p')
    person_data["end_time"] = datetime.datetime.strptime(
        person_cfg["end_time"], '%b %d %Y %I:%M%p')
    person_data["duration"] = (
        person_data["end_time"] - person_data["start_time"])
    size = int((person_data["end_time"] - person_data["start_time"]).total_seconds() /
               (60*configData["interval"]))

    person_data["social_bubble"] = []

    person_data["other_connections"] = other_connections

    generateHeartRate(size, person_cfg, person_data)
    generateRespiratoryRate(size, person_cfg, person_data)
    generateSkinTemp(size, person_cfg, person_data)
    generateSleepEpisode(size, person_cfg, person_data)

    return person_data


def generatePassword(length):
    valid_chars = string.ascii_lowercase + \
        string.ascii_uppercase + string.digits + string.punctuation
    return ''.join(random.choices(valid_chars, k=length))


def outputPerson(person_data):
    with open(FOLDER + person_data["name"].replace(" ", "_") + ".json", "w") as f:
        json.dump(person_data, f, default=myconverter, indent=4)


def myconverter(o):
    if isinstance(o, datetime.datetime) or isinstance(o, datetime.timedelta):
        return o.__str__()


def generatePeople():
    households = {}
    links = {}
    for household in configData["households"]:

        household_name = household["household"]
        household_postcode = household["postcode"]
        other_connections = household["other_connections"]
        households[household_name] = []
        links[household_name] = {}
        for link in household["connections"]:
            links[household_name][link] = False

        for person in household["members"]:
            households[household_name].append(generatePerson(
                person, household_postcode, household_name, other_connections))

    generateMainBubble(households, households[configData["main_household"]][0],
                       links)
    generateSocialBubbles(households, links)

    return households


def addConnection(person1, person2, links):
    person1["social_bubble"].append(person2["username"])
    person2["social_bubble"].append(person1["username"])
    links[person1["household"]].pop(person2["household"], None)
    links[person2["household"]].pop(person1["household"], None)


def generateMainBubble(households, person, links):
    for (name, household) in households.items():
        if (person["household"] == name):
            continue
        for hh_person in household:
            addConnection(person, hh_person, links)


def generateSocialBubbles(households, links):
    for link in links:
        while links[link]:
            other_name = links[link].popitem()[0]
            person1 = randomPersonFromHouse(households[link])
            person2 = randomPersonFromHouse(households[other_name])
            addConnection(person1, person2, links)
            print(person1["name"] + " : " + person2["name"])


def randomPersonFromHouse(household):
    return random.choice(household)


def outputPeople(households):
    for (name, household) in households.items():
        for person in household:
            outputPerson(person)


households = generatePeople()
outputPeople(households)
