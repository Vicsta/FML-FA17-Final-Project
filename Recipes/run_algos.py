from sklearn.svm import SVC
from sklearn.ensemble import AdaBoostClassifier
from sklearn.tree import DecisionTreeClassifier
from sklearn.model_selection import cross_val_score, train_test_split
from sklearn.preprocessing import MinMaxScaler
from sklearn.metrics import confusion_matrix
import json
import time
import re
from random import shuffle, randint


def read_data():
    data = []
    for i in range(1, 20111):
        with open("Recipes_RAW/JSON/recipe" + str(i) + ".txt", "r") as f:
            data.append(json.load(f))
    return data


def combine_files(a, b):

    data_a = []
    data_b = []

    start = time.time()
    with open(a, 'U') as f:
        for line in f:
            clean = line.strip()
            if len(clean) > 0:
                data_a.append(clean)

    with open(b, 'U') as f:
        for line in f:
            clean = line.strip()
            if len(clean) > 0:
                data_b.append(clean)
    assert len(data_a) == len(data_b), "Lengths are not the same"

    length = None
    with open('recipes_combined', 'w') as f:
        for i in range(len(data_a)):
            split_a = data_a[i].split(",")
            split_b = data_b[i].split(",")
            rating_b = split_b[0]
            combined_features = [rating_b]
            combined_features += split_a[1:] + split_b[1:]

            if length:
                assert len(combined_features) == length, print("Mismatched lengths")
            else:
                length = len(combined_features)

            f.write(",".join(combined_features) + "\n")
    print("Finished features in", time.time() - start, "seconds")

    return "recipes_combined"


def make_features_weights_sparse():
    start = time.time()

    output_file = open('recipes_weights_sparse', 'w')
    with open('ingredients_sparse_weights.txt', 'r') as f:
        for line in f:
            clean = line.strip()
            if len(clean) > 0:
                split = clean.split(" ")
                features = list()
                rating = split[0]
                split = split[1].split(",")
                features.append(rating)
                total = 0.
                for i in range(len(split)):
                    total += float(split[i])
                if total == 0:
                    continue
                for i in range(len(split)):
                    features.append(str(float(split[i]) / total))
                output_file.write(",".join(features) + "\n")
        output_file.close()
    print("Finished features in", time.time() - start, "seconds")

    return "recipes_weights_sparse"


def make_features_weights():
    start = time.time()

    output_file = open('recipes_weights', 'w')
    with open('ingredients_grouped_weights.txt', 'r') as f:
        for line in f:
            clean = line.strip()
            if len(clean) > 0:
                split = clean.split(" ")
                features = list()
                rating = split[0]
                split = split[1].split(",")
                features.append(rating)
                total = 0.
                for i in range(len(split)):
                    total += float(split[i])
                if total == 0:
                    continue
                for i in range(len(split)):
                    features.append(str(float(split[i]) / total))
                output_file.write(",".join(features) + "\n")
        output_file.close()
    print("Finished features in", time.time() - start, "seconds")

    return "recipes_weights"


def make_features_directions():
    keywords = []
    with open('directions', 'r') as f:
        for line in f:
            clean = line.strip()
            if len(clean) > 0:
                keywords.append(clean)

    errs = 0
    start = time.time()
    data = read_data()

    temp_regex = re.compile("(?P<temp>\d+)°[Ff]")
    minute_regex = re.compile("(?P<min>\d+) [mh]")
    hour_regex = re.compile("(?P<hr>\d+) [h]")

    output_file = open('recipes_directions', 'w')

    for recipe in data:
        if 'ingredients' not in recipe or 'rating' not in recipe or recipe['rating'] is None:
            errs += 1
            continue

        features = []
        rating = int(round(float(recipe['rating']), 0))
        features.append(str(rating))

        if "directions" not in recipe or recipe['directions'] is None or len(recipe['directions']) == 0:
            features += ["0"] * (len(keywords) + 2)
        else:
            direction_string = " ".join(recipe['directions']).lower()

            for i in keywords:
                if i in direction_string:
                    features.append("1")
                else:
                    features.append("0")

            res = re.search(temp_regex, direction_string)
            if not res:
                features.append("0")
            else:
                features.append(res.group('temp'))

            res = re.search(minute_regex, direction_string)
            if not res:
                minutes = 0
            else:
                minutes = float(res.group('min'))

            res = re.search(hour_regex, direction_string)
            if not res:
                minutes += 0
            else:
                minutes += 60 * float(res.group('hr'))
            features.append(str(minutes))

        output_file.write(",".join(features) + "\n")

    output_file.close()
    print("Finished features in", time.time() - start, "seconds")
    print("Errors:", errs)
    return "recipes_directions"


def make_features_easy():
    start = time.time()

    ingredients = []
    with open('ingredients.txt', 'r') as f:
        for line in f:
            clean = line.strip()
            if len(clean) > 0:
                ingredients.append(clean)
    print("Read", len(ingredients), "ingredients in", time.time() - start, "seconds")

    start = time.time()
    errs = 0
    data = read_data()

    output_file = open('recipes_easy', 'w')

    for recipe in data:
        if 'ingredients' not in recipe or 'rating' not in recipe or recipe['rating'] is None:
            errs += 1
            continue

        features = []
        rating = int(round(float(recipe['rating']), 0))
        features.append(str(rating))

        for ingredient in ingredients:
            found = False
            for ingredient_recipe in recipe['ingredients']:
                if ingredient in ingredient_recipe:
                    found = True
                    break
            if found:
                features.append("1")
            else:
                features.append("0")

        output_file.write(",".join(features))
        output_file.write("\n")

    output_file.close()
    print("Finished making features in", time.time() - start, "seconds")
    print("Errors:", errs)
    return "recipes_easy"


def make_features_sparse():
    ingredients = []
    mappings = {}

    with open('ingredients_sparse.txt', 'r') as f:
        for line in f:
            key = False
            values = []
            split = line.split(",")
            for i in range(len(split)):
                ingredient = split[i]
                cleaned = ingredient.strip()
                if len(cleaned) > 0:
                    if not key:
                        key = cleaned
                        ingredients.append(cleaned)
                    else:
                        values.append(cleaned)
            mappings[key] = values
    ingredients.sort()
    ingredients.sort(key=len, reverse=True)

    start = time.time()
    data = read_data()

    errs = 0
    output_file = open('recipes_sparse', 'w')
    for recipe in data:
        if 'ingredients' not in recipe or 'rating' not in recipe or recipe['rating'] is None:
            errs += 1
            continue

        features = []
        rating = int(round(float(recipe['rating']), 0))
        features.append(str(rating))

        for ingredient in ingredients:
            found = False
            for ingredient_recipe in recipe['ingredients']:
                if ingredient in ingredient_recipe:
                    found = True
                    break
                if ingredient in mappings:
                    for m in mappings[ingredient]:
                        if m in ingredient_recipe:
                            found = True
                            break
                if found:
                    break
            if found:
                features.append("1")
            else:
                features.append("0")

        output_file.write(",".join(features))
        output_file.write("\n")

    output_file.close()
    print("Finished making features in", time.time() - start, "seconds")
    print("Errors:", errs)
    return "recipes_sparse"


def make_features_grouped(freq=False):

    start = time.time()
    ingredients = []
    mappings = {}
    number = 0

    with open('ingredients_grouped.txt', 'r') as f:
        for line in f:
            key = False
            values = []
            split = line.split(",")
            for i in range(len(split)):
                ingredient = split[i]
                cleaned = ingredient.strip()
                if len(cleaned) > 0:
                    number += 1
                    if not key:
                        key = cleaned
                        ingredients.append(cleaned)
                    else:
                        values.append(cleaned)
            mappings[key] = values
    ingredients.sort()
    ingredients.sort(key=len, reverse=True)

    data = read_data()

    errs = 0
    output_file = open('recipes_grouped', 'w')
    length = None
    for recipe in data:
        if 'ingredients' not in recipe or 'rating' not in recipe or recipe['rating'] is None:
            errs += 1
            continue

        features = []
        rating = int(round(float(recipe['rating']), 0))
        features.append(str(rating))

        for ingredient in ingredients:
            found = 0
            for ingredient_recipe in recipe['ingredients']:
                if ingredient in ingredient_recipe:
                    found += 1
                if ingredient in mappings:
                    for m in mappings[ingredient]:
                        if m in ingredient_recipe:
                            found += 1
            features.append(found)

        total = 0
        for i in range(1, len(features)):
            total += features[i]
        if total == 0:
            total = 1

        if freq:
            for i in range(1, len(features)):
                features[i] = str(features[i] / total)
        else:
            for i in range(1, len(features)):
                if features[i] > 0:
                    features[i] = "1"
                else:
                    features[i] = "0"

        if length:
            assert len(features) == length
        else:
            length = len(features)

        output_file.write(",".join(features))
        output_file.write("\n")

    output_file.close()
    print("Finished making features in", time.time() - start, "seconds")
    print("Errors:", errs)
    return "recipes_grouped"


def make_ranking_features(filename, num=1000):

    start = time.time()

    data = []
    for i in range(4):
        data.append([])

    with open(filename) as f:
        for line in f:
            clean = line.strip()
            if len(clean) > 0:
                rating = int(line.split(",")[0])
                if rating <= 2:
                    data[0].append(clean)
                else:
                    data[rating - 2].append(clean)

    for l in range(len(data)):
        print("Number of", l + 2, ":", len(data[l]))

    picked = []
    for l in data:
        shuffle(l)
        picked += l[:num]
    data = picked

    print("Finished reading file in", time.time() - start, "seconds")
    print("Found", len(data), "total recipes")
    if num > len(data):
        print("Specified number is larger than dataset...")
        num = len(data)
    shuffle(data)
    print("Starting rank features...")
    start = time.time()

    count = 0
    output_file = open(filename + "_rank", "w")
    for i in range(num):
        for j in range(num):
            if i == j:
                continue
            if count % 10000 == 0:
                print(count)
            features = []
            split_i = data[i].split(",")
            split_j = data[j].split(",")
            rating_i = int(split_i[0])
            rating_j = int(split_j[0])
            if rating_i < rating_j:
                features.append("-1")
            elif rating_j < rating_i:
                features.append("1")
            else:
                features.append("0")
            features += split_i[1:]
            features += split_j[1:]
            output_file.write(",".join(features) + "\n")
            count += 1
    output_file.close()
    print("Finished writing ranking features in", time.time() - start, "seconds")


def get_occurrences(y):
    occur = {}
    for i in y:
        if str(i) in occur:
            occur[str(i)] += 1
        else:
            occur[str(i)] = 1
    return occur, len(y)


def split_data(filename, scaled=False, equalize=False, n_classes=None, output=False):
    all_data = {}
    length = None
    with open(filename, "U") as f:
        for line in f:
            clean = line.strip()
            if len(clean) > 0:
                split = clean.split(',')
                split_int = [float(i) for i in split]
                rating = int(split_int[0])
                if n_classes is not None:
                    if n_classes == 4:
                        if rating <= 2:
                            rating = 2
                            split_int[0] = 2
                    elif n_classes == 3:
                        if rating <= 3:
                            rating = 3
                            split_int[0] = 3
                    elif n_classes == 2:
                        if rating <= 3:
                            rating = -1
                            split_int[0] = -1
                        else:
                            rating = 1
                            split_int[0] = 1
                if rating not in all_data:
                    all_data[rating] = []
                all_data[rating].append(split_int)
                if length:
                    assert length == len(split_int)
                else:
                    length = len(split_int)

    all = []
    if equalize:
        most = -1
        for k in all_data:
            if most == -1:
                most = len(all_data[k])
            else:
                most = min(most, len(all_data[k]))

        for k in all_data:
            all += all_data[k][:most]

    else:
        for k in all_data:
            all += all_data[k]

    shuffle(all)
    X = []
    y = []
    for i in all:
        X += [i[1:]]
        y += [int(i[0])]
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=.1)

    print("Train size:", len(X_train))
    print("Test size:", len(X_test))

    if scaled:
        min_max_scaler = MinMaxScaler(feature_range=(0, 1))
        train_scaled = min_max_scaler.fit_transform(X_train)
        test_scaled = min_max_scaler.transform(X_test)
        return train_scaled, y_train, test_scaled, y_test
    else:
        if output:
            with open(filename + "_train", "w") as f:
                for i in range(len(X_train)):
                    features = [str(j) for j in X_train[i]]
                    f.write(",".join(features))
                    f.write("," + str(y_train[i] - (5 - n_classes + 1)) + "\n")
            with open(filename + "_test", "w") as f:
                for i in range(len(X_test)):
                    features = [str(j) for j in X_train[i]]
                    f.write(",".join(features))
                    f.write("," + str(y_test[i] - (5 - n_classes + 1)) + "\n")
        return X_train, y_train, X_test, y_test


def create_ranking_vectors(filename, max_lines=None):
    start = time.time()

    output_file = open(filename + "_rank", "w")
    with open(filename, "r") as f:
        data = []
        for line in f:
            clean = line.strip()
            if len(clean) > 0:
                data.append(clean)
        s = set()
        counter = 0
        while counter < max_lines:
            i = randint(0, len(data) - 1)
            j = randint(0, len(data) - 1)
            if (i, j) in s or (j, i) in s or i == j:
                continue

            split_i = data[i].split(",")
            split_j = data[j].split(",")
            rating_i = int(split_i[0])
            rating_j = int(split_j[0])
            features = []
            if rating_i < rating_j:
                features.append("-1")
            elif rating_j < rating_i:
                features.append("1")
            else:
                features.append("0")
            features += split_i[1:] + split_j[1:]
            output_file.write(",".join(features) + "\n")
            s.add((i, j))
            counter += 1

    output_file.close()

    print("Finished in", time.time() - start, "seconds")
    return filename + "_rank"


def run_svm(X_train, y_train, X_test, y_test, cv=True):

    C_s = [pow(2, x) for x in range(12, 13)]
    d_s = [2]

    for d in d_s:
        for C in C_s:
            svc = SVC(max_iter=1000000, kernel="poly", C=C, degree=d)

            print("\nStarting for C=%f, d=%d" % (C, d))
            if cv:
                cv_errs = cross_val_score(svc, X_train, y_train, cv=10, n_jobs=-1)
                cv_err = 1. - sum(cv_errs) / len(cv_errs)
                print("Cross validation error for C=%d, d=%d is: %f" % (C, d, cv_err))
            print("Starting SVM testing....")
            svc.fit(X_train, y_train)
            y_pred = svc.predict(X_test)
            acc = svc.score(X_test, y_test)
            print(confusion_matrix(y_test, y_pred))
            print(y_pred)
            print("Test error for C=%d, d=%d is: %f" % (C, d, 1. - acc))
            print("Closeness for C=%d, d=%d is: %f" % (C, d, get_closeness(y_pred, y_test)))


def run_adaboost(X_train, y_train, X_test, y_test, cv=True):

    occur, total = get_occurrences(y_test)
    print("In test...")
    for i in occur:
        print(i, "occurs", occur[i], "times (probability:", str(occur[i] / total) + ')')

    for n in [50, 100, 200, 500, 800]:
        print("Starting Adaboost for", n, "iterations")
        clf = AdaBoostClassifier(n_estimators=n, base_estimator=DecisionTreeClassifier(max_depth=1))
        if cv:
            start = time.time()
            cv_errs = cross_val_score(clf, X_train, y_train, cv=10)
            cv_err = 1. - sum(cv_errs) / len(cv_errs)
            print("Finished in", time.time() - start, "seconds. Cross validation error for", n, "iterations is:", cv_err)
        clf.fit(X_train, y_train)
        y_pred = clf.predict(X_test)
        acc = clf.score(X_test, y_test)
        print("Test error for", n, "iterations is:", 1. - acc)
        print("Closeness for", n, "iterations is:", get_closeness(y_pred, y_test))


def get_closeness(y_pred, y_test):
    assert len(y_pred) == len(y_test)
    n = len(y_pred)
    total = 0
    for i in range(len(y_pred)):
        total += abs(y_pred[i] - y_test[i])
    return total / n


if __name__ == "__main__":

    # data = read_data()
    # ratings = [0] * 6
    # for recipe in data:
    #     if 'rating' in recipe and recipe['rating'] is not None:
    #         ratings[int(round(float(recipe['rating'])))] += 1
    # print("Total:", sum(ratings))
    # for i in range(len(ratings)):
    #     print(i, "occurs", ratings[i], "probability", ratings[i] / sum(ratings))


    # combine_files(make_features_grouped(), make_features_directions())
    # X_train, y_train, X_test, y_test = split_data("recipes_combined", n_classes=4, equalize=True, scaled=True)
    # run_svm(X_train, y_train, X_test, y_test, cv=False)

    # make_features_grouped(freq=True)
    # X_train, y_train, X_test, y_test = split_data("recipes_grouped", n_classes=2, equalize=True, scaled=True)
    # run_svm(X_train, y_train, X_test, y_test, cv=False)

    # combine_files(make_features_sparse(), make_features_directions())
    # X_train, y_train, X_test, y_test = split_data("recipes_combined", n_classes=4, equalize=True, scaled=True)
    # run_svm(X_train, y_train, X_test, y_test, cv=False)

    # combine_files(make_features_sparse(), make_features_directions())
    # X_train, y_train, X_test, y_test = split_data("recipes_combined", n_classes=3, equalize=True, scaled=True)
    # run_svm(X_train, y_train, X_test, y_test, cv=False)

    # make_features_sparse()
    # X_train, y_train, X_test, y_test = split_data("recipes_sparse", n_classes=3, equalize=True, scaled=True)
    # run_svm(X_train, y_train, X_test, y_test, cv=False)

    # make_features_easy()
    # X_train, y_train, X_test, y_test = split_data("recipes_easy", n_classes=4, equalize=True, scaled=True)
    # run_svm(X_train, y_train, X_test, y_test, cv=False)

    # combine_files(make_features_grouped(), make_features_directions())
    # X_train, y_train, X_test, y_test = split_data("recipes_combined", n_classes=2, equalize=True, scaled=False)
    # run_adaboost(X_train, y_train, X_test, y_test, cv=False)

    # make_features_grouped()
    # X_train, y_train, X_test, y_test = split_data("recipes_grouped", n_classes=4, equalize=True, scaled=True)
    # run_svm(X_train, y_train, X_test, y_test, cv=False)

    # X_train, y_train, X_test, y_test = split_data("recipes_sparse", n_classes=2, equalize=True, scaled=False)
    # run_adaboost(X_train, y_train, X_test, y_test, cv=False)

    # X_train, y_train, X_test, y_test = split_data("recipes_easy", n_classes=2, equalize=True, scaled=False)
    # run_adaboost(X_train, y_train, X_test, y_test, cv=False)

    l = []
    with open('test', 'r') as f:
        for line in f:
            for num in line.strip().split(" "):
                l.append(num)
    freq = [0] * 6
    for i in l:
        freq[int(i)] += 1
    print(freq)
